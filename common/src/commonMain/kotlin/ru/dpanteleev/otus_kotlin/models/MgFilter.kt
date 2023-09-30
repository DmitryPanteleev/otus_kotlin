package ru.dpanteleev.otus_kotlin.models

data class MgFilter(
	var searchString: String = "",
	var ownerId: MortgageId = MortgageId.NONE,
	var borrowerCategoryModel: BorrowerCategoryModel = BorrowerCategoryModel.NONE,
)
