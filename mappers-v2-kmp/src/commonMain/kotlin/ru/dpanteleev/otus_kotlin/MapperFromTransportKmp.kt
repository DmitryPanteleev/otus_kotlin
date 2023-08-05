package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.exceptions.UnknownClass
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.FilterRequest
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.Rate
import ru.dpanteleev.otus_kotlin.models.RequestId
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.models.BorrowerCategory
import ru.dpanteleev.otus_kotlin.models.IRequest
import ru.dpanteleev.otus_kotlin.models.MgCreateObject
import ru.dpanteleev.otus_kotlin.models.MgCreateRequest
import ru.dpanteleev.otus_kotlin.models.MgDeleteRequest
import ru.dpanteleev.otus_kotlin.models.MgOffersRequest
import ru.dpanteleev.otus_kotlin.models.MgReadRequest
import ru.dpanteleev.otus_kotlin.models.MgRequestDebugMode
import ru.dpanteleev.otus_kotlin.models.MgRequestDebugStubs
import ru.dpanteleev.otus_kotlin.models.MgSearchFilter
import ru.dpanteleev.otus_kotlin.models.MgSearchRequest
import ru.dpanteleev.otus_kotlin.models.MgUpdateObject
import ru.dpanteleev.otus_kotlin.models.MgUpdateRequest
import ru.dpanteleev.otus_kotlin.models.MgVisibility
import ru.dpanteleev.otus_kotlin.stubs.MgStubs

fun Context.fromTransport(request: IRequest) = when (request) {
	is MgCreateRequest -> fromTransport(request)
	is MgReadRequest -> fromTransport(request)
	is MgUpdateRequest -> fromTransport(request)
	is MgDeleteRequest -> fromTransport(request)
	is MgSearchRequest -> fromTransport(request)
	is MgOffersRequest -> fromTransport(request)
	else -> throw UnknownClass(request::class)
}

private fun IRequest?.requestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE
private fun String?.toMgId() = this?.let { MortgageId(it.toLong()) } ?: MortgageId.NONE
private fun String?.toMgWithId() = Mortgage(this.toMgId())
private fun MgSearchFilter?.toInternal(): FilterRequest = FilterRequest(
	searchString = this?.searchString ?: ""
)

private fun MgCreateObject?.toInternal(): Mortgage = Mortgage(
	title = this?.title ?: "",
	description = this?.description ?: "",
	borrowerCategoryModel = this?.borrowerCategory?.toBorrowerCategoryModel() ?: BorrowerCategoryModel.SALARY,
	rate = this?.rate?.let { Rate(it) } ?: Rate.NONE,
	bankId = this?.bankId?.let { BankId(it) } ?: BankId.NONE,
	visibility = this?.visibility?.toVisibilityModel() ?: Visibility.OWNER_ONLY
)

private fun MgUpdateObject?.toInternal(): Mortgage = Mortgage(
	id = this?.id.toMgId(),
	title = this?.title ?: "",
	description = this?.description ?: "",
	borrowerCategoryModel = this?.borrowerCategory?.toBorrowerCategoryModel() ?: BorrowerCategoryModel.SALARY,
	rate = this?.rate?.let { Rate(it) } ?: Rate.NONE,
	bankId = this?.bankId?.let { BankId(it) } ?: BankId.NONE,
	visibility = this?.visibility?.toVisibilityModel() ?: Visibility.OWNER_ONLY
)

private fun BorrowerCategory.toBorrowerCategoryModel() = when (this) {
	BorrowerCategory.EMPLOYEE -> BorrowerCategoryModel.EMPLOYEE
	BorrowerCategory.SALARY -> BorrowerCategoryModel.SALARY
	BorrowerCategory.CONFIRM_INCOME -> BorrowerCategoryModel.CONFIRM_INCOME
	BorrowerCategory.NOT_CONFIRM_INCOME -> BorrowerCategoryModel.NOT_CONFIRM_INCOME
}

private fun MgVisibility.toVisibilityModel() = when (this) {
	MgVisibility.OWNER_ONLY -> Visibility.OWNER_ONLY
	MgVisibility.PUBLIC -> Visibility.PUBLIC
	MgVisibility.REGISTERED_ONLY -> Visibility.REGISTERED_ONLY
}

private fun MgRequestDebugMode.toWorkMode() = when (this) {
	MgRequestDebugMode.PROD -> WorkMode.PROD
	MgRequestDebugMode.STUB -> WorkMode.STUB
	MgRequestDebugMode.TEST -> WorkMode.TEST
}

private fun MgRequestDebugStubs.toStub() = when (this) {
	MgRequestDebugStubs.SUCCESS -> MgStubs.SUCCESS
	MgRequestDebugStubs.BAD_DESCRIPTION -> MgStubs.BAD_DESCRIPTION
	MgRequestDebugStubs.BAD_ID -> MgStubs.BAD_ID
	MgRequestDebugStubs.BAD_TITLE -> MgStubs.BAD_TITLE
	MgRequestDebugStubs.BAD_SEARCH_STRING -> MgStubs.BAD_SEARCH_STRING
	MgRequestDebugStubs.BAD_VISIBILITY -> MgStubs.BAD_VISIBILITY
	MgRequestDebugStubs.CANNOT_DELETE -> MgStubs.CANNOT_DELETE
	MgRequestDebugStubs.NOT_FOUND -> MgStubs.NOT_FOUND
}

fun Context.fromTransport(request: MgCreateRequest) {
	command = Command.CREATE
	requestId = request.requestId()
	mortgageRequest = request.mg.toInternal()
	workMode = request.debug?.mode?.toWorkMode() ?: WorkMode.PROD
	stubCase = request.debug?.stub?.toStub() ?: MgStubs.NOT_FOUND
}

fun Context.fromTransport(request: MgReadRequest) {
	command = Command.READ
	requestId = request.requestId()
	mortgageRequest = request.mg?.id.toMgWithId()
	workMode = request.debug?.mode?.toWorkMode() ?: WorkMode.PROD
	stubCase = request.debug?.stub?.toStub() ?: MgStubs.NOT_FOUND
}

fun Context.fromTransport(request: MgUpdateRequest) {
	command = Command.READ
	requestId = request.requestId()
	mortgageRequest = request.mg?.id.toMgWithId()
	workMode = request.debug?.mode?.toWorkMode() ?: WorkMode.PROD
	stubCase = request.debug?.stub?.toStub() ?: MgStubs.NOT_FOUND
}

fun Context.fromTransport(request: MgDeleteRequest) {
	command = Command.DELETE
	requestId = request.requestId()
	mortgageRequest = request.mg?.id.toMgWithId()
	workMode = request.debug?.mode?.toWorkMode() ?: WorkMode.PROD
	stubCase = request.debug?.stub?.toStub() ?: MgStubs.NOT_FOUND
}

fun Context.fromTransport(request: MgOffersRequest) {
	command = Command.OFFERS
	requestId = request.requestId()
	mortgageRequest = request.mg?.id.toMgWithId()
	workMode = request.debug?.mode?.toWorkMode() ?: WorkMode.PROD
	stubCase = request.debug?.stub?.toStub() ?: MgStubs.NOT_FOUND
}

fun Context.fromTransport(request: MgSearchRequest) {
	command = Command.SEARCH
	requestId = request.requestId()
	filterRequest = request.mgFilter.toInternal()
	workMode = request.debug?.mode?.toWorkMode() ?: WorkMode.PROD
	stubCase = request.debug?.stub?.toStub() ?: MgStubs.NOT_FOUND
}

