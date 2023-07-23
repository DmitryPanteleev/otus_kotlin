package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.exceptions.UnknownCommand
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.MgPermissionClient
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.models.Visibility
import ru.dpanteleev.otus_kotlin.api.v2.models.BorrowerCategory
import ru.dpanteleev.otus_kotlin.api.v2.models.IResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgCreateResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgDeleteResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgInitResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgOffersResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgPermissions
import ru.dpanteleev.otus_kotlin.api.v2.models.MgReadResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgResponseObject
import ru.dpanteleev.otus_kotlin.api.v2.models.MgSearchResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgUpdateResponse
import ru.dpanteleev.otus_kotlin.api.v2.models.MgVisibility
import ru.dpanteleev.otus_kotlin.api.v2.models.ResponseResult
import ru.dpanteleev.otus_kotlin.api.v2.models.Error


fun Context.toTransport(): IResponse = when (val cmd = command) {
	Command.CREATE -> toTransportCreate()
	Command.READ -> toTransportRead()
	Command.UPDATE -> toTransportUpdate()
	Command.DELETE -> toTransportDelete()
	Command.SEARCH -> toTransportSearch()
	Command.OFFERS -> toTransportOffers()
	else -> throw UnknownCommand(cmd)
}

fun Context.toTransportCreate() = MgCreateResponse(
	responseType = "create",
	requestId = this.requestId.asString().takeIf { it.isNotBlank() },
	result = if (state == State.ACTIVE) ResponseResult.SUCCESS else ResponseResult.ERROR,
	errors = errors.toTransportErrors(),
	mg = mortgageResponse.toTransport()?.takeIf { it.size == 1 }?.first()
)

fun Context.toTransportRead() = MgReadResponse(
	responseType = "read",
	requestId = this.requestId.asString().takeIf { it.isNotBlank() },
	result = if (state == State.ACTIVE) ResponseResult.SUCCESS else ResponseResult.ERROR,
	errors = errors.toTransportErrors(),
	mg = mortgageResponse.toTransport()?.takeIf { it.size == 1 }?.first()
)

fun Context.toTransportUpdate() = MgUpdateResponse(
	responseType = "update",
	requestId = this.requestId.asString().takeIf { it.isNotBlank() },
	result = if (state == State.ACTIVE) ResponseResult.SUCCESS else ResponseResult.ERROR,
	errors = errors.toTransportErrors(),
	mg = mortgageResponse.toTransport()?.takeIf { it.size == 1 }?.first()
)

fun Context.toTransportDelete() = MgDeleteResponse(
	responseType = "delete",
	requestId = this.requestId.asString().takeIf { it.isNotBlank() },
	result = if (state == State.ACTIVE) ResponseResult.SUCCESS else ResponseResult.ERROR,
	errors = errors.toTransportErrors(),
	mg = mortgageResponse.toTransport()?.takeIf { it.size == 1 }?.first()
)

fun Context.toTransportSearch() = MgSearchResponse(
	responseType = "search",
	requestId = this.requestId.asString().takeIf { it.isNotBlank() },
	result = if (state == State.ACTIVE) ResponseResult.SUCCESS else ResponseResult.ERROR,
	errors = errors.toTransportErrors(),
	mgs = mortgageResponse.toTransport()
)

fun Context.toTransportOffers() = MgOffersResponse(
	responseType = "offers",
	requestId = this.requestId.asString().takeIf { it.isNotBlank() },
	result = if (state == State.ACTIVE) ResponseResult.SUCCESS else ResponseResult.ERROR,
	errors = errors.toTransportErrors(),
	mgs = mortgageResponse.toTransport()
)

fun List<Mortgage>.toTransport(): List<MgResponseObject>? = this
	.map { it.toTransport() }
	.toList()
	.takeIf { it.isNotEmpty() }

fun Context.toTransportInit() = MgInitResponse(
	responseType = "init",
	requestId = this.requestId.asString().takeIf { it.isNotBlank() },
	result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
	errors = errors.toTransportErrors(),
)

private fun Mortgage.toTransport(): MgResponseObject = MgResponseObject(
	id = id.takeIf { it != MortgageId.NONE }?.asString(),
	title = title.takeIf { it.isNotBlank() },
	description = description.takeIf { it.isNotBlank() },
	bankId = bankId.takeIf { it != BankId.NONE }?.asLong(),
	borrowerCategory = borrowerCategoryModel.toTransport(),
	visibility = visibility.toTransport(),
	permissions = permissionsClient.toTransport(),
	productId = id.takeIf { it != MortgageId.NONE }?.asString(),
	rate = rate.asLong()
)

private fun Set<MgPermissionClient>.toTransport(): Set<MgPermissions>? = this
	.map { it.toTransport() }
	.toSet()
	.takeIf { it.isNotEmpty() }

private fun MgPermissionClient.toTransport() = when (this) {
	MgPermissionClient.READ -> MgPermissions.READ
	MgPermissionClient.UPDATE -> MgPermissions.UPDATE
	MgPermissionClient.MAKE_VISIBLE_OWNER -> MgPermissions.MAKE_VISIBLE_OWN
	MgPermissionClient.MAKE_VISIBLE_GROUP -> MgPermissions.MAKE_VISIBLE_GROUP
	MgPermissionClient.MAKE_VISIBLE_PUBLIC -> MgPermissions.MAKE_VISIBLE_PUBLIC
	MgPermissionClient.DELETE -> MgPermissions.DELETE
}

private fun Visibility.toTransport(): MgVisibility? = when (this) {
	Visibility.PUBLIC -> MgVisibility.PUBLIC
	Visibility.REGISTERED_ONLY -> MgVisibility.REGISTERED_ONLY
	Visibility.OWNER_ONLY -> MgVisibility.OWNER_ONLY
}

private fun List<MgError>.toTransportErrors(): List<Error>? = this
	.map { it.toTransport() }
	.toList()
	.takeIf { it.isNotEmpty() }

private fun MgError.toTransport() = Error(
	code = code.takeIf { it.isNotBlank() },
	group = group.takeIf { it.isNotBlank() },
	field = field.takeIf { it.isNotBlank() },
	message = message.takeIf { it.isNotBlank() },
)

private fun BorrowerCategoryModel.toTransport() = when (this) {
	BorrowerCategoryModel.EMPLOYEE -> BorrowerCategory.EMPLOYEE
	BorrowerCategoryModel.SALARY -> BorrowerCategory.SALARY
	BorrowerCategoryModel.CONFIRM_INCOME -> BorrowerCategory.CONFIRM_INCOME
	BorrowerCategoryModel.NOT_CONFIRM_INCOME -> BorrowerCategory.NOT_CONFIRM_INCOME
}