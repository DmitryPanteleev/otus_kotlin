package ru.dpanteleev.otus_kotlin

import kotlinx.datetime.Clock
import ru.dpanteleev.otus_kotlin.helpers.asMgError
import ru.dpanteleev.otus_kotlin.helpers.fail
import ru.dpanteleev.otus_kotlin.logging.common.IMpLogWrapper
import ru.dpanteleev.otus_kotlin.models.Command

suspend fun <T> MgProcessor.process(
    logger: IMpLogWrapper,
    logId: String,
    fromTransport: suspend Context.() -> Unit,
    sendResponse: suspend Context.() -> T,
    toLog: Context.(logId: String) -> Any): T {
    var command = Command.NONE
    return try {
        val ctx = Context(
            timeStart = Clock.System.now(),
        )

        logger.doWithLogging(id = logId) {
            ctx.fromTransport()
            command = ctx.command

            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            exec(ctx)
            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )
            ctx.sendResponse()
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            logger.error(msg = "$command handling failed")

            val ctx = Context(
                timeStart = Clock.System.now(),
                command = command
            )

            ctx.fail(e.asMgError())
            exec(ctx)
            sendResponse(ctx)
        }
    }
}
