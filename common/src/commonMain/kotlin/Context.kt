import kotlinx.datetime.Instant
import models.Command
import models.FilterRequest
import models.MgError
import models.Mortgage
import models.RequestId
import models.State
import stubs.MgStubs
import models.WorkMode

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
	val mortgageResponse: MutableList<Mortgage> = mutableListOf()
)