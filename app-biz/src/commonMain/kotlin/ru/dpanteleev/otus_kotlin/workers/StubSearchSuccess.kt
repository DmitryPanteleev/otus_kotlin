package ru.dpanteleev.otus_kotlin.workers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.Stub
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.stubs.MgStubs
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.stubSearchSuccess(title: String) = worker {
	this.title = title
	on { stubCase == MgStubs.SUCCESS && state == State.ACTIVE }
	handle {
		state = State.FINISHING
		mortgageResponse.addAll(Stub.prepareSearchList(filterRequest.searchString, BorrowerCategoryModel.SALARY))
	}
}
