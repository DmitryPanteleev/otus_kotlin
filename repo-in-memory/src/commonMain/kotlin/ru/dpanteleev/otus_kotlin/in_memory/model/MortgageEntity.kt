package ru.dpanteleev.otus_kotlin.in_memory.model

import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.Visibility


data class MortgageEntity(
	val id: String? = null,
	val title: String? = null,
	val description: String? = null,
	val bankId: Long? = null,
	val borrowerCategoryModel: String? = null,
	val visibility: String? = null,
	val lock: String? = null,
) {
	constructor(model: Mortgage) : this(
		id = model.id.asString().takeIf { it.isNotBlank() },
		title = model.title.takeIf { it.isNotBlank() },
		description = model.description.takeIf { it.isNotBlank() },
		bankId = model.bankId.takeIf { it != BankId.NONE }?.asLong(),
		borrowerCategoryModel = model.borrowerCategoryModel.takeIf { it != BorrowerCategoryModel.NONE }?.name,
		visibility = model.visibility.takeIf { it != Visibility.NONE }?.name,
		lock = model.lock.asString().takeIf { it.isNotBlank() }
	)

	fun toInternal() = Mortgage(
		id = id?.let { MortgageId(it) } ?: MortgageId.NONE,
		title = title ?: "",
		description = description ?: "",
		bankId = bankId?.let { BankId(it) } ?: BankId.NONE,
		borrowerCategoryModel = borrowerCategoryModel?.let { BorrowerCategoryModel.valueOf(it) } ?: BorrowerCategoryModel.NONE,
		visibility = visibility?.let { Visibility.valueOf(it) } ?: Visibility.NONE,
		lock = lock?.let { MgLock(it) } ?: MgLock.NONE,
	)
}
