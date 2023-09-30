package ru.dpanteleev.otus_kotlin.validation

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.finishMgValidation(title: String) = worker {
	this.title = title
	on { state == State.ACTIVE }
	handle {
		mgValidated = mgValidating
	}
}

fun ICoreChainDsl<Context>.finishAdFilterValidation(title: String) = worker {
	this.title = title
	on { state == State.ACTIVE }
	handle {
		filterRequest = filterRequest
	}
}
