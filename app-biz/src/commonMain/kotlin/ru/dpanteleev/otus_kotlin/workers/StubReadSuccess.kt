package ru.dpanteleev.otus_kotlin.workers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.Stub
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.stubs.MgStubs
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.stubReadSuccess(title: String) = worker {
	this.title = title
	on { stubCase == MgStubs.SUCCESS && state == State.ACTIVE }
	handle {
		state = State.FINISHING
		val stub = Stub.prepareResult {
			mortgageRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
		}
		mortgageResponse = mutableListOf(stub)
	}
}
