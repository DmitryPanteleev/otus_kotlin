package ru.dpanteleev.otus_kotlin.validation

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.helpers.errorValidation
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.validateLockNotEmpty(title: String) = worker {
	this.title = title
	on { mgValidating.lock.asString().isEmpty() }
	handle {
		fail(
			errorValidation(
				field = "lock",
				violationCode = "empty",
				description = "field must not be empty"
			)
		)
	}
}
