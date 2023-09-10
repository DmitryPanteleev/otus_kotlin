package ru.otus.otuskotlin.marketplace.core.handlers

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.otus.otuskotlin.marketplace.core.CoreDslMarker
import ru.otus.otuskotlin.marketplace.core.ICoreChainDsl
import ru.otus.otuskotlin.marketplace.core.ICoreExec
import ru.otus.otuskotlin.marketplace.core.ICoreExecDsl

/**
 * Реализация цепочки (chain), которая исполняет свои вложенные цепочки и рабочие
 * в соответствии со стратегией handler
 */
class CoreChain<T>(
	private val execs: List<ICoreExec<T>>,
	private val handler: suspend (T, List<ICoreExec<T>>) -> Unit,
	title: String,
	description: String = "",
	blockOn: suspend T.() -> Boolean = { true },
	blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCoreExec<T>(title, description, blockOn, blockExcept) {
	override suspend fun handle(context: T) = handler(context, execs)
}

/**
 * Стратегия последовательного исполнения
 */
suspend fun <T> executeSequential(context: T, execs: List<ICoreExec<T>>): Unit =
	execs.forEach {
		it.exec(context)
	}

/**
 * Стратегия параллельного исполнения
 */
suspend fun <T> executeParallel(context: T, execs: List<ICoreExec<T>>): Unit = coroutineScope {
	execs.forEach {
		launch { it.exec(context) }
	}
}

@CoreDslMarker
class CoreChainDsl<T>(
	private val handler: suspend (T, List<ICoreExec<T>>) -> Unit = ::executeSequential,
) : CorExecDsl<T>(), ICoreChainDsl<T> {
	private val workers: MutableList<ICoreExecDsl<T>> = mutableListOf()
	override fun add(worker: ICoreExecDsl<T>) {
		workers.add(worker)
	}

	override fun build(): ICoreExec<T> = CoreChain(
		title = title,
		description = description,
		execs = workers.map { it.build() },
		handler = handler,
		blockOn = blockOn,
		blockExcept = blockExcept
	)
}
