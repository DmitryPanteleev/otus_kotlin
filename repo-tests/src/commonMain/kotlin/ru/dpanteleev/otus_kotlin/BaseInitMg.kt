package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.Visibility

abstract class BaseInitMg(val op: Long) : IInitObjects<Mortgage> {

	open val lockOld: MgLock = MgLock("20000000-0000-0000-0000-000000000001")
	open val lockBad: MgLock = MgLock("20000000-0000-0000-0000-000000000009")

	fun createInitTestModel(
		suf: Long,
		bankId: BankId = BankId.NONE,
		borrowerType: BorrowerCategoryModel = BorrowerCategoryModel.NONE,
		lock: MgLock = lockOld,
	) = Mortgage(
		id = MortgageId("$op-$suf"),
		title = "$suf stub",
		description = "$suf stub description",
		bankId = bankId,
		visibility = Visibility.OWNER_ONLY,
		borrowerCategoryModel = borrowerType,
		lock = lock,
	)
}
