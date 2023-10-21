package ru.dpanteleev.otus_kotlin.repo

interface IMgRepository {
    suspend fun createMg(rq: DbMgRequest): DbMgResponse
    suspend fun readMg(rq: DbMgIdRequest): DbMgResponse
    suspend fun updateMg(rq: DbMgRequest): DbMgResponse
    suspend fun deleteMg(rq: DbMgIdRequest): DbMgResponse
    suspend fun searchMg(rq: DbMgFilterRequest): DbMgsResponse
    companion object {
        val NONE = object : IMgRepository {
            override suspend fun createMg(rq: DbMgRequest): DbMgResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readMg(rq: DbMgIdRequest): DbMgResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateMg(rq: DbMgRequest): DbMgResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteMg(rq: DbMgIdRequest): DbMgResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchMg(rq: DbMgFilterRequest): DbMgsResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
