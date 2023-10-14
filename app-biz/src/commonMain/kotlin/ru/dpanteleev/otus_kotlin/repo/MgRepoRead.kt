package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoRead(title: String) = worker {
	this.title = title
	description = "Чтение объявления из БД"
	on { state == State.ACTIVE }
	handle {
		val request = DbMgIdRequest(mgValidated)
		val result = mgRepo.readMg(request)
		val resultMg = result.data
		if (result.isSuccess && resultMg != null) {
			mgRepoRead = resultMg
		} else {
			state = State.FAILING
			errors.addAll(result.errors)
		}
	}
}
