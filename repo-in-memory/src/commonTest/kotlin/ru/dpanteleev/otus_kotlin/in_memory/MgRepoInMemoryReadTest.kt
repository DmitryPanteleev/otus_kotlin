package ru.dpanteleev.otus_kotlin.in_memory

import ru.dpanteleev.otus_kotlin.RepoMgReadTest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository


class MgRepoInMemoryReadTest: RepoMgReadTest() {
    override val repo: IMgRepository = MgRepoInMemory(
        initObjects = initObjects
    )
}
