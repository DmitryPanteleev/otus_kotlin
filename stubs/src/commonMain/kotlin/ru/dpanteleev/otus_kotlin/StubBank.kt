package ru.dpanteleev.otus_kotlin
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.MgPermissionClient
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.Rate
import ru.dpanteleev.otus_kotlin.models.Visibility

object StubBank {
    val DEMAND_BANK1: Mortgage
        get() = Mortgage(
            id = MortgageId(666),
            title = "Новостройка",
            description = "Квартира в новостройке",
            bankId = BankId(1),
            borrowerCategoryModel = BorrowerCategoryModel.SALARY,
            visibility = Visibility.PUBLIC,
            permissionsClient = mutableSetOf(
                MgPermissionClient.READ,
                MgPermissionClient.UPDATE,
                MgPermissionClient.DELETE,
                MgPermissionClient.MAKE_VISIBLE_PUBLIC,
                MgPermissionClient.MAKE_VISIBLE_GROUP,
                MgPermissionClient.MAKE_VISIBLE_OWNER,
            ),
            rate = Rate(10.0)
        )
    val SUPPLY_BANK1 = DEMAND_BANK1.copy(borrowerCategoryModel = BorrowerCategoryModel.SALARY)
}
