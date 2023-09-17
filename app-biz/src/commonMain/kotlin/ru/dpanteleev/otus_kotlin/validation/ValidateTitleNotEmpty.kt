package ru.dpanteleev.otus_kotlin.validation

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.helpers.errorValidation
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

// TODO-validation-4: смотрим пример COR DSL валидации
fun ICoreChainDsl<Context>.validateTitleNotEmpty(title: String) = worker {
	this.title = title
	on { mgValidating.title.isEmpty() }
	handle {
		fail(
			errorValidation(
				field = "title",
				violationCode = "empty",
				description = "field must not be empty"
			)
		)
	}
}
