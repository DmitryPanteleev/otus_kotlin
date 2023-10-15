package ru.dpanteleev.otus_kotlin.postgress

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.dpanteleev.otus_kotlin.NONE
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.Rate
import ru.dpanteleev.otus_kotlin.models.Visibility

object MortgageTable : Table("mortgage") {
	val id = varchar("id", 128)
	val title = varchar("title", 128)
	val description = varchar("description", 512)
	val bankId = long("bank_id")
	val borrowerCategoryModel = enumeration<BorrowerCategoryModel>("borrower_category")
	val visibility = enumeration<Visibility>("visibility")
	val rate = double("rate")
	val lock = varchar("lock", 50)
	val timePublished = varchar("time_published", 30)
	val timeUpdated = varchar("time_updated", 30)

	override val primaryKey = PrimaryKey(id)

	fun from(res: InsertStatement<Number>) = Mortgage(
		id = MortgageId(res[id]),
		title = res[title],
		description = res[description],
		bankId = BankId(res[bankId]),
		visibility = res[visibility],
		borrowerCategoryModel = res[borrowerCategoryModel],
		lock = MgLock(res[lock]),
		timePublished = Instant.parse(res[timePublished]),
		timeUpdated = Instant.parse(res[timeUpdated]),
		rate = Rate(res[rate])
	)

	fun from(res: ResultRow) = Mortgage(
		id = MortgageId(res[id]),
		title = res[title],
		description = res[description],
		bankId = BankId(res[bankId]),
		visibility = res[visibility],
		borrowerCategoryModel = res[borrowerCategoryModel],
		lock = MgLock(res[lock]),
		timePublished = Instant.parse(res[timePublished]),
		timeUpdated = Instant.parse(res[timeUpdated]),
		rate = Rate(res[rate])
	)

	fun to(it: UpdateBuilder<*>, mortgage: Mortgage, randomUuid: () -> String) {
		it[id] = mortgage.id.takeIf { it != MortgageId.NONE }?.asString() ?: randomUuid()
		it[title] = mortgage.title
		it[description] = mortgage.description
		it[bankId] = mortgage.bankId.asLong()
		it[visibility] = mortgage.visibility
		it[borrowerCategoryModel] = mortgage.borrowerCategoryModel
		it[lock] = mortgage.lock.takeIf { it != MgLock.NONE }?.asString() ?: randomUuid()
		it[rate] = mortgage.rate.asLong()
		it[timePublished] = mortgage.timePublished.takeIf { it != Instant.NONE }?.toString() ?: Clock.System.now().toString()
		it[timeUpdated] = mortgage.timeUpdated.toString()
	}

}
