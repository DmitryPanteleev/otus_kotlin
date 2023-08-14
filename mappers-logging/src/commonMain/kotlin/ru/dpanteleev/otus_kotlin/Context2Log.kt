package ru.dpanteleev.otus_kotlin

import kotlinx.datetime.Clock
import ru.dpanteleev.otus_kotlin.api.logs.models.CommonLogModel
import ru.dpanteleev.otus_kotlin.api.logs.models.ErrorLogModel
import ru.dpanteleev.otus_kotlin.api.logs.models.MgFilterLog
import ru.dpanteleev.otus_kotlin.api.logs.models.MgLog
import ru.dpanteleev.otus_kotlin.api.logs.models.MgLogModel
import ru.dpanteleev.otus_kotlin.models.BankId
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.FilterRequest
import ru.dpanteleev.otus_kotlin.models.MgError
import ru.dpanteleev.otus_kotlin.models.Mortgage
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.Rate
import ru.dpanteleev.otus_kotlin.models.RequestId
import ru.dpanteleev.otus_kotlin.models.Visibility


fun Context.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "mortgage_place",
    mg = toMkplLog(),
    errors = errors.map { it.toLog() }
)

fun Context.toMkplLog(): MgLogModel? {
    val mortgageNone = Mortgage()
    return MgLogModel(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        requestAd = mortgageRequest.takeIf { it != mortgageNone }?.toLog(),
        responseAds = mortgageResponse.takeIf { it.isNotEmpty() }?.filter { it != mortgageNone }?.map { it.toLog() },
        requestFilter = filterRequest.takeIf { it != FilterRequest() }?.toLog(),
    ).takeIf { it != MgLogModel() }
}

private fun FilterRequest.toLog() = MgFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    bankId = bankId.takeIf { it != BankId.NONE }?.asString()
)

fun MgError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun Mortgage.toLog() = MgLog(
    id = id.takeIf { it != MortgageId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    borrowerCategoryModel = borrowerCategoryModel.takeIf { it != BorrowerCategoryModel.NONE }?.name,
    visibility = visibility.takeIf { it != Visibility.NONE }?.name,
    rate = rate.takeIf { it != Rate.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)
