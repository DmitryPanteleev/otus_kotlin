package ru.dpanteleev.otus_kotlin.permissions

import ru.dpanteleev.otus_kotlin.models.BankId

data class MgPrincipalModel(
	val id: BankId = BankId.NONE,
	val bname: String = "",
	val groups: Set<MgUserGroups> = emptySet()
) {
	companion object {
		val NONE = MgPrincipalModel()
	}
}
