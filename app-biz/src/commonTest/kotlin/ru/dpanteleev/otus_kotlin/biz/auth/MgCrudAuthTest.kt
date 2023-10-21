package ru.dpanteleev.otus_kotlin.biz.auth

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.CoreSettings
import ru.dpanteleev.otus_kotlin.MgProcessor
import ru.dpanteleev.otus_kotlin.Stub
import ru.dpanteleev.otus_kotlin.in_memory.MgRepoInMemory
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgPermissionClient
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.permissions.MgPrincipalModel
import ru.dpanteleev.otus_kotlin.permissions.MgUserGroups

/**
 * @crud - экземпляр класса-фасада бизнес-логики
 * @context - контекст, смапленный из транспортной модели запроса
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MgCrudAuthTest {
	@Test
	fun createSuccessTest() = runTest {
		val userId = BankId(123)
		val repo = MgRepoInMemory()
		val processor = MgProcessor(
			settings = CoreSettings(
				repoTest = repo
			)
		)
		val context = Context(
			workMode = WorkMode.TEST,
			mortgageRequest = Stub.prepareResult {
				permissionsClient.clear()
				id = MortgageId.NONE
			},
			command = Command.CREATE,
			principal = MgPrincipalModel(
				id = userId,
				groups = setOf(
					MgUserGroups.USER,
					MgUserGroups.TEST,
				)
			)
		)

		processor.exec(context)
		assertEquals(State.FINISHING, context.state)
		with(context.mortgageResponse.first()) {
			assertTrue { id.asString().isNotBlank() }
//			assertContains(permissionsClient, MgPermissionClient.READ)
//			assertContains(permissionsClient, MgPermissionClient.UPDATE)
//			assertContains(permissionsClient, MgPermissionClient.DELETE)
//            assertFalse { permissionsClient.contains(PermissionModel.CONTACT) }
		}
	}

	@Test
	fun readSuccessTest() = runTest {
		val mgObj = Stub.get()
		val bankId = mgObj.bankId
		val mgId = mgObj.id
		val repo = MgRepoInMemory(initObjects = listOf(mgObj))
		val processor = MgProcessor(
			settings = CoreSettings(
				repoTest = repo
			)
		)
		val context = Context(
			command = Command.READ,
			workMode = WorkMode.TEST,
			mortgageRequest = Mortgage(id = mgId),
			principal = MgPrincipalModel(
				id = bankId,
				groups = setOf(
					MgUserGroups.USER,
					MgUserGroups.TEST,
				)
			)
		)
		processor.exec(context)
		assertEquals(State.FINISHING, context.state)
		with(context.mortgageResponse.first()) {
			assertTrue { id.asString().isNotBlank() }
//			assertContains(permissionsClient, MgPermissionClient.READ)
//			assertContains(permissionsClient, MgPermissionClient.UPDATE)
//			assertContains(permissionsClient, MgPermissionClient.DELETE)
//            assertFalse { context.responseAd.permissions.contains(PermissionModel.CONTACT) }
		}
	}

}
