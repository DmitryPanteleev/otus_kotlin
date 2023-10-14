package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel

data class DbMgFilterRequest(
	val titleFilter: String = "",
	val bankId: BankId = BankId.NONE,
	val borrowerCategoryModel: BorrowerCategoryModel = BorrowerCategoryModel.NONE,
)
