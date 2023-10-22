package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoOffers(title: String) = worker {
	this.title = title
	description = "Поиск предложений для объявления по названию"
	on { state == State.ACTIVE }
	handle {
		val mgRequest = mgRepoPrepare
		val filter = DbMgFilterRequest(
			titleFilter = mgRequest.title,
			borrowerCategoryModel = mgRequest.borrowerCategoryModel
		)
		val dbResponse = if (filter.borrowerCategoryModel == BorrowerCategoryModel.NONE) {
			DbMgsResponse(
				data = null,
				isSuccess = false,
				errors = listOf(
					MgError(
						field = "mgType",
						message = "Type of mg must not be empty"
					)
				)
			)
		} else {
			mgRepo.searchMg(filter)
		}

		val resultAds = dbResponse.data
		when {
			!resultAds.isNullOrEmpty() -> mgsRepoDone = resultAds.toMutableList()
			dbResponse.isSuccess -> return@handle
			else -> {
				state = State.FAILING
				errors.addAll(dbResponse.errors)
			}
		}
	}
}
