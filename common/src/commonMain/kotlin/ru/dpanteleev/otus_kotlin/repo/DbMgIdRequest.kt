package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId

data class DbMgIdRequest(
	val id: MortgageId,
	val lock: MgLock = MgLock.NONE,
) {
	constructor(mg: Mortgage) : this(mg.id, mg.lock)
}
