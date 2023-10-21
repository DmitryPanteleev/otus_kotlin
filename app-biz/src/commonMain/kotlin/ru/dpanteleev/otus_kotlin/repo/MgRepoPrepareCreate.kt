package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoPrepareCreate(title: String) = worker {
	this.title = title
	description = "Подготовка объекта к сохранению в базе данных"
	on { state == State.ACTIVE }
	handle {
		mgRepoRead = mgValidated.deepCopy()
		mgRepoRead.bankId = principal.id
		mgRepoPrepare = mgRepoRead

	}
}
