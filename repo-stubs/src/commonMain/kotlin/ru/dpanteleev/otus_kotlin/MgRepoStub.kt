package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.repo.DbMgFilterRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgIdRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgRequest
import ru.dpanteleev.otus_kotlin.repo.DbMgResponse
import ru.dpanteleev.otus_kotlin.repo.DbMgsResponse
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

class MgRepoStub() : IMgRepository {
	override suspend fun createMg(rq: DbMgRequest): DbMgResponse {
		return DbMgResponse(
			data = Stub.prepareResult { },
			isSuccess = true,
		)
	}

	override suspend fun readMg(rq: DbMgIdRequest): DbMgResponse {
		return DbMgResponse(
			data = Stub.prepareResult { },
			isSuccess = true,
		)
	}

	override suspend fun updateMg(rq: DbMgRequest): DbMgResponse {
		return DbMgResponse(
			data = Stub.prepareResult { },
			isSuccess = true,
		)
	}

	override suspend fun deleteMg(rq: DbMgIdRequest): DbMgResponse {
		return DbMgResponse(
			data = Stub.prepareResult { },
			isSuccess = true,
		)
	}

	override suspend fun searchMg(rq: DbMgFilterRequest): DbMgsResponse {
		return DbMgsResponse(
			data = Stub.prepareSearchList(filter = "", BorrowerCategoryModel.SALARY),
			isSuccess = true,
		)
	}
}
