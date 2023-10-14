package ru.dpanteleev.otus_kotlin.biz.statemachine

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import ru.dpanteleev.otus_kotlin.statemachine.SMMgSignal
import ru.dpanteleev.otus_kotlin.statemachine.SMMgStateResolver
import ru.dpanteleev.otus_kotlin.statemachine.SMStates

class SMMgStateTest {

	@Test
	fun new2actual() {
		val machine = SMMgStateResolver()
		val signal = SMMgSignal(
			state = SMStates.NEW,
			duration = 4.days,
			views = 20,
		)
		val transition = machine.resolve(signal)
		assertEquals(SMStates.ACTUAL, transition.state)
		assertContains(transition.description, "актуальное", ignoreCase = true)
	}

	@Test
	fun new2hit() {
		val machine = SMMgStateResolver()
		val signal = SMMgSignal(
			state = SMStates.NEW,
			duration = 2.days,
			views = 101,
		)
		val transition = machine.resolve(signal)
		assertEquals(SMStates.HIT, transition.state)
		assertContains(transition.description, "Очень", ignoreCase = true)
	}
}
