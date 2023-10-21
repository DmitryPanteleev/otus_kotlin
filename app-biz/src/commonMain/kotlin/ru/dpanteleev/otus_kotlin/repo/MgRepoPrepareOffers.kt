package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoPrepareOffers(title: String) = worker {
	this.title = title
	description = "Готовим данные к поиску предложений в БД"
	on { state == State.ACTIVE }
	handle {
		mgRepoPrepare = mgRepoRead.deepCopy()
		mgRepoDone = mgRepoRead.deepCopy()
	}
}
