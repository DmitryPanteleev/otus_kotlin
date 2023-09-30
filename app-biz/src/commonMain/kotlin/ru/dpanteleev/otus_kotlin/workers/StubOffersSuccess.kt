package ru.dpanteleev.otus_kotlin.workers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.Stub
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.stubs.MgStubs
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.stubOffersSuccess(title: String) = worker {
	this.title = title
	on { stubCase == MgStubs.SUCCESS && state == State.ACTIVE }
	handle {
		state = State.FINISHING
		mortgageResponse = mutableListOf(Stub.prepareResult {
			mortgageRequest.id.takeIf { it != MortgageId.NONE }?.also { this.id = it }
		})
		mortgageResponse.addAll(Stub.prepareOffersList(mortgageResponse.first().title, BorrowerCategoryModel.SALARY))
	}
}
