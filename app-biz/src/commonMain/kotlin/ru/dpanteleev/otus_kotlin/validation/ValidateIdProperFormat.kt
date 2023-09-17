package ru.dpanteleev.otus_kotlin.validation

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.helpers.errorValidation
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.validateIdProperFormat(title: String) = worker {
	this.title = title

	// Может быть вынесен в MkplAdId для реализации различных форматов
	val regExp = Regex("^[0-9a-zA-Z-]+$")
	on { mgValidating.id != MortgageId.NONE && !mgValidating.id.asString().matches(regExp) }
	handle {
		val encodedId = mgValidating.id.asString()
			.replace("<", "&lt;")
			.replace(">", "&gt;")
		fail(
			errorValidation(
				field = "id",
				violationCode = "badFormat",
				description = "value $encodedId must contain only letters and numbers"
			)
		)
	}
}
