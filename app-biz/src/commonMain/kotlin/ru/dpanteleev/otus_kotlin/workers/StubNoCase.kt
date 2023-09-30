package ru.dpanteleev.otus_kotlin.workers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.stubNoCase(title: String) = worker {
	this.title = title
	on { state == State.ACTIVE }
	handle {
		fail(
			MgError(
				code = "validation",
				field = "stub",
				group = "validation",
				message = "Wrong stub case is requested: ${stubCase.name}"
			)
		)
	}
}
