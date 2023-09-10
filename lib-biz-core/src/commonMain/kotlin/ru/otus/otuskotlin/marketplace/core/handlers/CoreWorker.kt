package ru.otus.otuskotlin.marketplace.core.handlers

import ru.otus.otuskotlin.marketplace.core.CoreDslMarker
import ru.otus.otuskotlin.marketplace.core.ICoreExec
import ru.otus.otuskotlin.marketplace.core.ICoreWorkerDsl

class CoreWorker<T>(
	title: String,
	description: String = "",
	blockOn: suspend T.() -> Boolean = { true },
	private val blockHandle: suspend T.() -> Unit = {},
	blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCoreExec<T>(title, description, blockOn, blockExcept) {
	override suspend fun handle(context: T) = blockHandle(context)
}

@CoreDslMarker
class CoreWorkerDsl<T> : CorExecDsl<T>(), ICoreWorkerDsl<T> {
	private var blockHandle: suspend T.() -> Unit = {}
	override fun handle(function: suspend T.() -> Unit) {
		blockHandle = function
	}

	override fun build(): ICoreExec<T> = CoreWorker(
		title = title,
		description = description,
		blockOn = blockOn,
		blockHandle = blockHandle,
		blockExcept = blockExcept
	)

}
