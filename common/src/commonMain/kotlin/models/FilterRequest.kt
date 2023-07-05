package models

data class FilterRequest(
	var searchString: String = "",
	var bankId: BankId = BankId.NONE
)
