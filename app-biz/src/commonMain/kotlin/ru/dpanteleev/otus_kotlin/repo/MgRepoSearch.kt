package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoSearch(title: String) = worker {
	this.title = title
	description = "Поиск объявлений в БД по фильтру"
	on { state == State.ACTIVE }
	handle {
		val request = DbMgFilterRequest(
			titleFilter = mgFilterValidated.searchString,
			bankId = mgFilterValidated.bankId,
			borrowerCategoryModel = mgFilterValidated.borrowerCategoryModel,
		)
		val result = mgRepo.searchMg(request)
		val resultAds = result.data
		if (result.isSuccess && resultAds != null) {
			mgsRepoDone = resultAds.toMutableList()
		} else {
			state = State.FAILING
			errors.addAll(result.errors)
		}
	}
}
