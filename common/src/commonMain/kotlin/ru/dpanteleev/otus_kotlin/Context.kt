package ru.dpanteleev.otus_kotlin

import kotlinx.datetime.Instant
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.FilterRequest
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.RequestId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.permissions.MgPrincipalModel
import ru.dpanteleev.otus_kotlin.repo.IMgRepository
import ru.dpanteleev.otus_kotlin.stubs.MgStubs

data class Context(
	var command: Command = Command.NONE,
	var state: State = State.ACTIVE,
	val errors: MutableList<MgError> = mutableListOf(),
	var workMode: WorkMode = WorkMode.PROD,
	var stubCase: MgStubs = MgStubs.NONE,
	var requestId: RequestId = RequestId.NONE,
	var timeStart: Instant = Instant.NONE,

	var mortgageRequest: Mortgage = Mortgage(),

	var filterRequest: FilterRequest = FilterRequest(),

	var mgValidating: Mortgage = Mortgage(),
	var mgFilterValidating: FilterRequest = FilterRequest(),

	var mgValidated: Mortgage = Mortgage(),
	var mgFilterValidated: FilterRequest = FilterRequest(),

	var mortgageResponse: MutableList<Mortgage> = mutableListOf(),

	var settings: CoreSettings = CoreSettings.NONE,

	var mgRepo: IMgRepository = IMgRepository.NONE,
	var mgRepoRead: Mortgage = Mortgage(),
	var mgRepoPrepare: Mortgage = Mortgage(),
	var mgRepoDone: Mortgage? = null,
	var mgsRepoDone: MutableList<Mortgage> = mutableListOf(),

	var principal: MgPrincipalModel = MgPrincipalModel.NONE,
	)