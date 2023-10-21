package ru.dpanteleev.otus_kotlin.general

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.helpers.errorAdministration
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.permissions.MgUserGroups
import ru.dpanteleev.otus_kotlin.repo.IMgRepository
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.initRepo(title: String) = worker {
	this.title = title
	description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
	handle {
		mgRepo = when {
			workMode == WorkMode.TEST -> settings.repoTest
			workMode == WorkMode.STUB -> settings.repoStub
			principal.groups.contains(MgUserGroups.TEST) -> settings.repoTest
			else -> settings.repoProd
		}
		if (workMode != WorkMode.STUB && mgRepo == IMgRepository.NONE) fail(
			errorAdministration(
				field = "repo",
				violationCode = "dbNotConfigured",
				description = "The database is unconfigured for chosen workmode ($workMode). " +
					"Please, contact the administrator staff"
			)
		)
	}
}
