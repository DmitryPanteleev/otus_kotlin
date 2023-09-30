package ru.dpanteleev.otus_kotlin.validation

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.helpers.errorValidation
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.validateLockProperFormat(title: String) = worker {
	this.title = title

	// Может быть вынесен в MkplAdId для реализации различных форматов
	val regExp = Regex("^[0-9a-zA-Z-]+$")
	on { mgValidating.lock != MgLock.NONE && !mgValidating.lock.asString().matches(regExp) }
	handle {
		val encodedId = mgValidating.lock.asString()
		fail(
			errorValidation(
				field = "lock",
				violationCode = "badFormat",
				description = "value $encodedId must contain only"
			)
		)
	}
}
