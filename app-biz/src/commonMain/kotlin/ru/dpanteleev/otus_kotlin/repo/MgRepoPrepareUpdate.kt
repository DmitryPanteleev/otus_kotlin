package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoPrepareUpdate(title: String) = worker {
	this.title = title
	description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
		"и данные, полученные от пользователя"
	on { state == State.ACTIVE }
	handle {
		mgRepoPrepare = mgRepoRead.deepCopy().apply {
			this.title = mgValidated.title
			description = mgValidated.description
			visibility = mgValidated.visibility
		}
	}
}
