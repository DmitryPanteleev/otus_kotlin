package ru.dpanteleev.otus_kotlin.biz

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.permissions.MgPrincipalModel
import ru.dpanteleev.otus_kotlin.permissions.MgUserGroups

fun Context.addTestPrincipal(bankId: BankId = BankId(321)) {
	principal = MgPrincipalModel(
		id = bankId,
		groups = setOf(
			MgUserGroups.USER,
			MgUserGroups.TEST,
		)
	)
}
