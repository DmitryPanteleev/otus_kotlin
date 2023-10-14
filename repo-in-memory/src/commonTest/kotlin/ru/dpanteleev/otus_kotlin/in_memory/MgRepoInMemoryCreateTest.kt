package ru.dpanteleev.otus_kotlin.in_memory

import ru.dpanteleev.otus_kotlin.RepoMgCreateTest

class MgRepoInMemoryCreateTest : RepoMgCreateTest() {
    override val repo = MgRepoInMemory(
        initObjects = initObjects,
        randomUuid = { lockNew.asString() }
    )
}