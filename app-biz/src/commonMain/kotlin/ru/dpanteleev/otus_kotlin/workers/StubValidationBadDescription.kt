package ru.dpanteleev.otus_kotlin.workers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.stubs.MgStubs
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.stubValidationBadDescription(title: String) = worker {
	this.title = title
	on { stubCase == MgStubs.BAD_DESCRIPTION && state == State.ACTIVE }
	handle {
		state = State.FAILING
		this.errors.add(
			MgError(
				group = "validation",
				code = "validation-description",
				field = "description",
				message = "Wrong description field"
			)
		)
	}
}
