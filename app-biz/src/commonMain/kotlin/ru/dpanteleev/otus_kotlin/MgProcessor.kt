package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.WorkMode

class MgProcessor {
	suspend fun exec(ctx: Context) {
		// TODO: Rewrite temporary stub solution with BIZ
		require(ctx.workMode == WorkMode.STUB) {
			"Currently working only in STUB mode."
		}

		when (ctx.command) {
			Command.SEARCH -> {
				ctx.mortgageResponse.addAll(Stub.prepareSearchList("Bank1>", BorrowerCategoryModel.EMPLOYEE))
			}

			Command.OFFERS -> {
				ctx.mortgageResponse.addAll(Stub.prepareOffersList("Bank2", BorrowerCategoryModel.CONFIRM_INCOME))
			}

			else -> {
				ctx.mortgageResponse = mutableListOf(Stub.get())
			}
		}
	}
}
