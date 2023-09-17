package ru.dpanteleev.otus_kotlin.general

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.chain

fun ICoreChainDsl<Context>.operation(title: String, command: Command, block: ICoreChainDsl<Context>.() -> Unit) =
	chain {
		block()
		this.title = title
		on { this.command == command && state == State.ACTIVE }
	}
