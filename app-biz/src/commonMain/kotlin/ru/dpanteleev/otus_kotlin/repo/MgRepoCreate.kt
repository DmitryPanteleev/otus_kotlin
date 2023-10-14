package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoCreate(title: String) = worker {
	this.title = title
	description = "Добавление объявления в БД"
	on { state == State.ACTIVE }
	handle {
		val request = DbMgRequest(mgRepoPrepare)
		val result = mgRepo.createMg(request)
		val resultMg = result.data
		if (result.isSuccess && resultMg != null) {
			mgRepoDone = resultMg
		} else {
			state = State.FAILING
			errors.addAll(result.errors)
		}
	}
}
