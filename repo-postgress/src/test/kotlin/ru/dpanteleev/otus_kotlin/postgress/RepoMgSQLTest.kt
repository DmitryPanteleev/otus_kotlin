package ru.dpanteleev.otus_kotlin.postgress

import ru.dpanteleev.otus_kotlin.RepoMgCreateTest
import ru.dpanteleev.otus_kotlin.RepoMgDeleteTest
import ru.dpanteleev.otus_kotlin.RepoMgReadTest
import ru.dpanteleev.otus_kotlin.RepoMgSearchTest
import ru.dpanteleev.otus_kotlin.RepoMgUpdateTest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

class RepoMgSQLCreateTest : RepoMgCreateTest() {
	override val repo: IMgRepository = SqlTestCompanion.repoUnderTestContainer(
		initObjects,
		randomUuid = { lockNew.asString() },
	)
}

class RepoMgSQLDeleteTest : RepoMgDeleteTest() {
	override val repo: IMgRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoMgSQLReMgTest : RepoMgReadTest() {
	override val repo: IMgRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoMgSQLSearchTest : RepoMgSearchTest() {
	override val repo: IMgRepository = SqlTestCompanion.repoUnderTestContainer(initObjects)
}

class RepoMgSQLUpdateTest : RepoMgUpdateTest() {
	override val repo: IMgRepository = SqlTestCompanion.repoUnderTestContainer(
		initObjects,
		randomUuid = { lockNew.asString() },
	)
}
