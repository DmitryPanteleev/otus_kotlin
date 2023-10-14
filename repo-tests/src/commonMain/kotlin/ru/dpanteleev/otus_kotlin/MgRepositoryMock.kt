package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.repo.DbMgsResponse
import ru.dpanteleev.otus_kotlin.repo.DbMgFilterRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgIdRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgResponse
import ru.dpanteleev.otus_kotlin.repo.DbMgRequest
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

class MgRepositoryMock(
    private val invokeCreateAd: (DbMgRequest) -> DbMgResponse = { DbMgResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadAd: (DbMgIdRequest) -> DbMgResponse = { DbMgResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateAd: (DbMgRequest) -> DbMgResponse = { DbMgResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteAd: (DbMgIdRequest) -> DbMgResponse = { DbMgResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchAd: (DbMgFilterRequest) -> DbMgsResponse = { DbMgsResponse.MOCK_SUCCESS_EMPTY },
): IMgRepository {
    override suspend fun createMg(rq: DbMgRequest): DbMgResponse {
        return invokeCreateAd(rq)
    }

    override suspend fun readMg(rq: DbMgIdRequest): DbMgResponse {
        return invokeReadAd(rq)
    }

    override suspend fun updateMg(rq: DbMgRequest): DbMgResponse {
        return invokeUpdateAd(rq)
    }

    override suspend fun deleteMg(rq: DbMgIdRequest): DbMgResponse {
        return invokeDeleteAd(rq)
    }

    override suspend fun searchMg(rq: DbMgFilterRequest): DbMgsResponse {
        return invokeSearchAd(rq)
    }
}
