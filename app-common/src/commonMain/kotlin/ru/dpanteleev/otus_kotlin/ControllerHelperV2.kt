package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.logging.common.IMpLogWrapper
import ru.dpanteleev.otus_kotlin.models.IRequest
import ru.dpanteleev.otus_kotlin.models.IResponse

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV2(
    processor: MgProcessor,
    request: Q,
    logger: IMpLogWrapper,
    logId: String,
): R  = processor.process(logger, logId,
        fromTransport = { fromTransport(request) },
        sendResponse = { toTransport() as R },
        toLog = { this.toLog(logId) },
)
