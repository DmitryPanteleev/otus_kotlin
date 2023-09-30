package ru.dpanteleev.otus_kotlin.validation

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.helpers.errorValidation
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

// TODO-validation-7: пример обработки ошибки в рамках бизнес-цепочки
fun ICoreChainDsl<Context>.validateDescriptionHasContent(title: String) = worker {
	this.title = title
	val regExp = Regex("\\p{L}")
	on { mgValidating.description.isNotEmpty() && !mgValidating.description.contains(regExp) }
	handle {
		fail(
			errorValidation(
				field = "description",
				violationCode = "noContent",
				description = "field must contain letters"
			)
		)
	}
}
