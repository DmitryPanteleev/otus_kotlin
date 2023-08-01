package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.StubBank.DEMAND_BANK1
import ru.dpanteleev.otus_kotlin.StubBank.SUPPLY_BANK1
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId

object Stub {
    fun get(): Mortgage = DEMAND_BANK1.copy()

    fun prepareResult(block: Mortgage.() -> Unit): Mortgage = get().apply(block)

    fun prepareSearchList(filter: String, type: BorrowerCategoryModel) = listOf(
        demand(6661, filter, type),
        demand(6662, filter, type),
        demand(6663, filter, type),
        demand(6664, filter, type),
        demand(6665, filter, type),
        demand(6666, filter, type),
    )

    fun prepareOffersList(filter: String, type: BorrowerCategoryModel) = listOf(
        supply(6661, filter, type),
        supply(6662, filter, type),
        supply(6663, filter, type),
        supply(6664, filter, type),
        supply(6665, filter, type),
        supply(6666, filter, type),
    )

    private fun demand(id: Long, filter: String, type: BorrowerCategoryModel) =
        mortgage(DEMAND_BANK1, id = id, filter = filter, type = type)

    private fun supply(id: Long, filter: String, type: BorrowerCategoryModel) =
        mortgage(SUPPLY_BANK1, id = id, filter = filter, type = type)

    private fun mortgage(base: Mortgage, id: Long, filter: String, type: BorrowerCategoryModel) = base.copy(
        id = MortgageId(id),
        title = "$filter $id",
        description = "desc $filter $id",
        borrowerCategoryModel = type,
    )

}
