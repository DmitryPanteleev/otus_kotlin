package ru.dpanteleev.otus_kotlin.models

data class Mortgage(
	var id: MortgageId = MortgageId.NONE,
	var title: String = "",
	var description: String = "",
	var bankId: BankId = BankId.NONE,
	var borrowerCategoryModel: BorrowerCategoryModel = BorrowerCategoryModel.NOT_CONFIRM_INCOME,
	var visibility: Visibility = Visibility.OWNER_ONLY,
	var rate: Rate = Rate.NONE,
	var permissionsClient: MutableSet<MgPermissionClient> = mutableSetOf()
)