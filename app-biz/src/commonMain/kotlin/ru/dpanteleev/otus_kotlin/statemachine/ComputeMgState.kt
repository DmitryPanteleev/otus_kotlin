package ru.dpanteleev.otus_kotlin.statemachine

import kotlin.reflect.KClass
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.NONE
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

private val machine = SMMgStateResolver()
private val clazz: KClass<*> = ICoreChainDsl<Context>::computeAdState::class

fun ICoreChainDsl<Context>.computeAdState(title: String) = worker {
	this.title = title
	this.description = "Вычисление состояния объявления"
	on { state == State.ACTIVE }
	handle {
		val log = settings.loggerProvider.logger(clazz)
		val timeNow = Clock.System.now()
		val mortgage = mgValidated
		val prevState = mortgage.mgState
		val timePublished = mortgage.timePublished.takeIf { it != Instant.NONE } ?: timeNow
		val signal = SMMgSignal(
			state = prevState.takeIf { it != SMStates.NONE } ?: SMStates.NEW,
			duration = timeNow - timePublished,
			views = mortgage.views,
		)
		val transition = machine.resolve(signal)
		if (transition.state != prevState) {
			log.info("New ad state transition: ${transition.description}")
		}
		mortgage.mgState = transition.state
	}
}
