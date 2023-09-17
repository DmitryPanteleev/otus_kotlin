package ru.dpanteleev.otus_kotlin.validation

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.helpers.errorValidation
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.validateTitleHasContent(title: String) = worker {
	this.title = title
	val regExp = Regex("\\p{L}")
	on { mgValidating.title.isNotEmpty() && !mgValidating.title.contains(regExp) }
	handle {
		fail(
			errorValidation(
				field = "title",
				violationCode = "noContent",
				description = "field must contain leters"
			)
		)
	}
}
