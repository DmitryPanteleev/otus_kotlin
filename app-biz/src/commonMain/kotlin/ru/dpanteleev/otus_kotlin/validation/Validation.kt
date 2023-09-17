package ru.dpanteleev.otus_kotlin.validation

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.chain

fun ICoreChainDsl<Context>.validation(block: ICoreChainDsl<Context>.() -> Unit) = chain {
	block()
	title = "Валидация"

	on { state == State.ACTIVE }
}
