package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoUpdate(title: String) = worker {
	this.title = title
	on { state == State.ACTIVE }
	handle {
		val request = DbMgRequest(mgRepoPrepare)
		val result = mgRepo.updateMg(request)
		val resultAd = result.data
		if (result.isSuccess && resultAd != null) {
			mgRepoDone = resultAd
		} else {
			state = State.FAILING
			errors.addAll(result.errors)
			mgRepoDone
		}
	}
}
