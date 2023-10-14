package ru.dpanteleev.otus_kotlin.postgress

import com.benasher44.uuid.uuid4
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.dpanteleev.otus_kotlin.helpers.asMgError
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.repo.DbMgFilterRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgIdRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgResponse
import ru.dpanteleev.otus_kotlin.repo.DbMgsResponse
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

class RepoMgSQL(
    properties: SqlProperties,
    initObjects: Collection<Mortgage> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : IMgRepository {

	init {
		val driver = when {
			properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
			else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
		}

		Database.connect(
			properties.url, driver, properties.user, properties.password
		)

		transaction {
			if (properties.dropDatabase) SchemaUtils.drop(MortgageTable)
			SchemaUtils.create(MortgageTable)
			initObjects.forEach { createMg(it) }
		}
	}

	private fun createMg(Mg: Mortgage): Mortgage {
		val res = MortgageTable.insert {
			to(it, Mg, randomUuid)
		}

		return MortgageTable.from(res)
	}

	private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
		try {
			transaction {
				block()
			}
		} catch (e: Exception) {
			handle(e)
		}

	private fun transactionWrapper(block: () -> DbMgResponse): DbMgResponse =
		transactionWrapper(block) { DbMgResponse.error(it.asMgError()) }

	override suspend fun createMg(rq: DbMgRequest): DbMgResponse = transactionWrapper {
		DbMgResponse.success(createMg(rq.mg))
	}

	private fun reMg(id: MortgageId): DbMgResponse {
		val res = MortgageTable.select {
			MortgageTable.id eq id.asString()
		}.singleOrNull() ?: return DbMgResponse.errorNotFound
		return DbMgResponse.success(MortgageTable.from(res))
	}

	override suspend fun readMg(rq: DbMgIdRequest): DbMgResponse = transactionWrapper { reMg(rq.id) }

	private fun update(
		id: MortgageId,
		lock: MgLock,
		block: (Mortgage) -> DbMgResponse
	): DbMgResponse =
		transactionWrapper {
			if (id == MortgageId.NONE) return@transactionWrapper DbMgResponse.errorEmptyId

			val current = MortgageTable.select { MortgageTable.id eq id.asString() }
				.firstOrNull()
				?.let { MortgageTable.from(it) }

			when {
				current == null -> DbMgResponse.errorNotFound
				current.lock != lock -> DbMgResponse.errorConcurrent(lock, current)
				else -> block(current)
			}
		}

	override suspend fun updateMg(rq: DbMgRequest): DbMgResponse =
		update(rq.mg.id, rq.mg.lock) {
			MortgageTable.update({
				(MortgageTable.id eq rq.mg.id.asString()) and (MortgageTable.lock eq rq.mg.lock.asString())
			}) {
				to(it, rq.mg, randomUuid)
			}
			reMg(rq.mg.id)
		}

	override suspend fun deleteMg(rq: DbMgIdRequest): DbMgResponse = update(rq.id, rq.lock) {
		MortgageTable.deleteWhere {
			(id eq rq.id.asString()) and (lock eq rq.lock.asString())
		}
		DbMgResponse.success(it)
	}

	override suspend fun searchMg(rq: DbMgFilterRequest): DbMgsResponse =
		transactionWrapper({
			val res = MortgageTable.select {
				buildList {
					add(Op.TRUE)
					if (rq.bankId != BankId.NONE) {
						add(MortgageTable.bankId eq rq.bankId.asLong())
					}
					if (rq.borrowerCategoryModel != BorrowerCategoryModel.NONE) {
						add(MortgageTable.borrowerCategoryModel eq rq.borrowerCategoryModel)
					}
					if (rq.titleFilter.isNotBlank()) {
						add(
							(MortgageTable.title like "%${rq.titleFilter}%")
								or (MortgageTable.description like "%${rq.titleFilter}%")
						)
					}
				}.reduce { a, b -> a and b }
			}
			DbMgsResponse(data = res.map { MortgageTable.from(it) }, isSuccess = true)
		}, {
			DbMgsResponse.error(it.asMgError())
		})
}
