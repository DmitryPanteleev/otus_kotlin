package ru.dpanteleev.otus_kotlin.in_memory

import ru.dpanteleev.otus_kotlin.RepoMgSearchTest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

class MgRepoInMemorySearchTest : RepoMgSearchTest() {
	override val repo: IMgRepository = MgRepoInMemory(
		initObjects = initObjects
	)
}
