package ru.dpanteleev.otus_kotlin.repo

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.State
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.worker

fun ICoreChainDsl<Context>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == State.ACTIVE }
    handle {
        val request = DbMgIdRequest(mgRepoPrepare)
        val result = mgRepo.deleteMg(request)
        if (!result.isSuccess) {
            state = State.FAILING
            errors.addAll(result.errors)
        }
        mgRepoDone = mgRepoRead
    }
}
