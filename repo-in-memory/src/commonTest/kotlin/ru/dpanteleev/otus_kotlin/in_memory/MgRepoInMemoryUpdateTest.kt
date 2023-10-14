package ru.dpanteleev.otus_kotlin.in_memory

import ru.dpanteleev.otus_kotlin.RepoMgUpdateTest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

class MgRepoInMemoryUpdateTest : RepoMgUpdateTest() {
	override val repo: IMgRepository = MgRepoInMemory(
		initObjects = initObjects,
		randomUuid = { lockNew.asString() }
	)
}
