package ru.dpanteleev.otus_kotlin.in_memory

import ru.dpanteleev.otus_kotlin.RepoMgDeleteTest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository


class MgRepoInMemoryDeleteTest : RepoMgDeleteTest() {
    override val repo: IMgRepository = MgRepoInMemory(
        initObjects = initObjects
    )
}
