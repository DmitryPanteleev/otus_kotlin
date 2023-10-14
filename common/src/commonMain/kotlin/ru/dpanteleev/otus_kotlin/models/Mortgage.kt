package ru.dpanteleev.otus_kotlin.models

import kotlinx.datetime.Instant
import ru.dpanteleev.otus_kotlin.NONE
import ru.dpanteleev.otus_kotlin.permissions.MgPrincipalRelations
import ru.dpanteleev.otus_kotlin.statemachine.SMStates

data class Mortgage(
	var id: MortgageId = MortgageId.NONE,
	var title: String = "",
	var description: String = "",
	var bankId: BankId = BankId.NONE,
	var borrowerCategoryModel: BorrowerCategoryModel = BorrowerCategoryModel.NOT_CONFIRM_INCOME,
	var visibility: Visibility = Visibility.OWNER_ONLY,
	var rate: Rate = Rate.NONE,
	var lock: MgLock = MgLock.NONE,
	var mgState: SMStates = SMStates.NONE,
	var timePublished: Instant = Instant.NONE,
	var timeUpdated: Instant = Instant.NONE,
	var views: Int = 0,
	var principalRelations: Set<MgPrincipalRelations> = emptySet(),
	val permissionsClient: MutableSet<MgPermissionClient> = mutableSetOf()
) {
	fun deepCopy(): Mortgage = copy(
		permissionsClient = permissionsClient.toMutableSet(),
	)

	fun isEmpty() = this == NONE

	companion object {
		val NONE = Mortgage()
	}
}
