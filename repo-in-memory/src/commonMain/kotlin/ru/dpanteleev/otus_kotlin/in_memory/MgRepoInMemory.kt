package ru.dpanteleev.otus_kotlin.in_memory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.dpanteleev.otus_kotlin.helpers.errorRepoConcurrency
import ru.dpanteleev.otus_kotlin.in_memory.model.MortgageEntity
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.repo.DbMgFilterRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgIdRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgResponse
import ru.dpanteleev.otus_kotlin.repo.DbMgsResponse
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

class MgRepoInMemory(
	initObjects: List<Mortgage> = emptyList(),
	ttl: Duration = 2.minutes,
	val randomUuid: () -> String = { uuid4().toString() },
) : IMgRepository {

	private val cache = Cache.Builder<String, MortgageEntity>()
		.expireAfterWrite(ttl)
		.build()
	private val mutex: Mutex = Mutex()

	init {
		initObjects.forEach {
			save(it)
		}
	}

	private fun save(mg: Mortgage) {
		val entity = MortgageEntity(mg)
		if (entity.id == null) {
			return
		}
		cache.put(entity.id, entity)
	}

	override suspend fun createMg(rq: DbMgRequest): DbMgResponse {
		val key = randomUuid()
		val mg = rq.mg.copy(id = MortgageId(key), lock = MgLock(randomUuid()))
		val entity = MortgageEntity(mg)
		cache.put(key, entity)
		return DbMgResponse(
			data = mg,
			isSuccess = true,
		)
	}

	override suspend fun readMg(rq: DbMgIdRequest): DbMgResponse {
		val key = rq.id.takeIf { it != MortgageId.NONE }?.asString() ?: return resultErrorEmptyId
		return cache.get(key)
			?.let {
				DbMgResponse(
					data = it.toInternal(),
					isSuccess = true,
				)
			} ?: resultErrorNotFound
	}

	override suspend fun updateMg(rq: DbMgRequest): DbMgResponse {
		val key = rq.mg.id.takeIf { it != MortgageId.NONE }?.asString() ?: return resultErrorEmptyId
		val oldLock = rq.mg.lock.takeIf { it != MgLock.NONE }?.asString() ?: return resultErrorEmptyLock
		val newAd = rq.mg.copy(lock = MgLock(randomUuid()))
		val entity = MortgageEntity(newAd)
		return mutex.withLock {
			val oldAd = cache.get(key)
			when {
				oldAd == null -> resultErrorNotFound
				oldAd.lock != oldLock -> DbMgResponse(
					data = oldAd.toInternal(),
					isSuccess = false,
					errors = listOf(errorRepoConcurrency(MgLock(oldLock), oldAd.lock?.let { MgLock(it) }))
				)

				else -> {
					cache.put(key, entity)
					DbMgResponse(
						data = newAd,
						isSuccess = true,
					)
				}
			}
		}
	}

	override suspend fun deleteMg(rq: DbMgIdRequest): DbMgResponse {
		val key = rq.id.takeIf { it != MortgageId.NONE }?.asString() ?: return resultErrorEmptyId
		val oldLock = rq.lock.takeIf { it != MgLock.NONE }?.asString() ?: return resultErrorEmptyLock
		return mutex.withLock {
			val oldMg = cache.get(key)
			when {
				oldMg == null -> resultErrorNotFound
				oldMg.lock != oldLock -> DbMgResponse(
					data = oldMg.toInternal(),
					isSuccess = false,
					errors = listOf(errorRepoConcurrency(MgLock(oldLock), oldMg.lock?.let { MgLock(it) }))
				)

				else -> {
					cache.invalidate(key)
					DbMgResponse(
						data = oldMg.toInternal(),
						isSuccess = true,
					)
				}
			}
		}
	}

	/**
	 * Поиск объявлений по фильтру
	 * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
	 */
	override suspend fun searchMg(rq: DbMgFilterRequest): DbMgsResponse =
		cache.asMap().asSequence()
			.filter { entry ->
				rq.bankId.takeIf { it != BankId.NONE }?.let {
					it.asLong() == entry.value.bankId
				} ?: true
			}
			.filter { entry ->
				rq.borrowerCategoryModel.takeIf { it != BorrowerCategoryModel.NONE }?.let {
					it.name == entry.value.borrowerCategoryModel
				} ?: true
			}
			.filter { entry ->
				rq.titleFilter.takeIf { it.isNotBlank() }?.let {
					entry.value.title?.contains(it) ?: false
				} ?: true
			}
			.map { it.value.toInternal() }
			.toList().let {
				DbMgsResponse(
					data = it,
					isSuccess = true
				)
			}

	companion object {
		val resultErrorEmptyId = DbMgResponse(
			isSuccess = false,
			data = null,
			errors = listOf(
				MgError(
					code = "id-empty",
					group = "validation",
					field = "id",
					message = "Id must not be null or blank"
				)
			)
		)
		val resultErrorEmptyLock = DbMgResponse(
			isSuccess = false,
			data = null,
			errors = listOf(
				MgError(
					code = "lock-empty",
					group = "validation",
					field = "lock",
					message = "Lock must not be null or blank"
				)
			)
		)
		val resultErrorNotFound = DbMgResponse(
			isSuccess = false,
			data = null,
			errors = listOf(
				MgError(
					code = "not-found",
					field = "id",
					message = "Not Found"
				)
			)
		)
	}
}
