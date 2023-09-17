package ru.dpanteleev.otus_kotlin.workers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.stubs.MgStubs
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.stubValidationBadId(title: String) = worker {
	this.title = title
	on { stubCase == MgStubs.BAD_ID && state == State.ACTIVE }
	handle {
		state = State.FAILING
		this.errors.add(
			MgError(
				group = "validation",
				code = "validation-id",
				field = "id",
				message = "Wrong id field"
			)
		)
	}
}
