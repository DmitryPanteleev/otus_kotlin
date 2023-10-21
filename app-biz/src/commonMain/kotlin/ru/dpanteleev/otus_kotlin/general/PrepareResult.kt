package ru.dpanteleev.otus_kotlin.general

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.prepareResult(title: String) = worker {
	this.title = title
	description = "Подготовка данных для ответа клиенту на запрос"
	on { workMode != WorkMode.STUB }
	handle {
		mortgageResponse = mgsRepoDone.plus(mgRepoDone) as MutableList<Mortgage>
		state = when (val st = state) {
			State.ACTIVE -> State.FINISHING
			else -> st
		}
	}
}
