package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoPrepareDelete(title: String) = worker {
	this.title = title
	description = """
        Готовим данные к удалению из БД
    """.trimIndent()
	on { state == State.ACTIVE }
	handle {
		mgRepoPrepare = mgValidated.deepCopy()
	}
}
