package ru.dpanteleev.otus_kotlin.general

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.chain

fun ICoreChainDsl<Context>.stubs(title: String, block: ICoreChainDsl<Context>.() -> Unit) = chain {
	block()
	this.title = title
	on { workMode == WorkMode.STUB && state == State.ACTIVE }
}
