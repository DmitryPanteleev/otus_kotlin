package ru.dpanteleev.otus_kotlin.models

data class FilterRequest(
	var searchString: String = "",
	var bankId: BankId = BankId.NONE,
	var borrowerCategoryModel: BorrowerCategoryModel = BorrowerCategoryModel.NONE,
)
