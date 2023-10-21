package ru.dpanteleev.otus_kotlin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.repo.DbMgFilterRequest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository


@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoMgSearchTest {
    abstract val repo: IMgRepository

    protected open val initializedObjects: List<Mortgage> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchMg(DbMgFilterRequest(bankId = bankOwnerId))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchDealSide() = runRepoTest {
        val result = repo.searchMg(DbMgFilterRequest(borrowerCategoryModel = BorrowerCategoryModel.SALARY))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitMg(1) {

        val bankOwnerId = BankId(4321)
        override val initObjects: List<Mortgage> = listOf(
            createInitTestModel(1),
            createInitTestModel(2, bankId = bankOwnerId),
            createInitTestModel(3, borrowerType = BorrowerCategoryModel.SALARY),
            createInitTestModel(4, bankId = bankOwnerId),
            createInitTestModel(5, borrowerType = BorrowerCategoryModel.SALARY)
        )
    }
}
