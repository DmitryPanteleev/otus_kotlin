package ru.otus.otuskotlin.marketplace.core

import ru.otus.otuskotlin.marketplace.core.handlers.CoreChainDsl
import ru.otus.otuskotlin.marketplace.core.handlers.CoreWorkerDsl
import ru.otus.otuskotlin.marketplace.core.handlers.executeParallel

/**
 * Базовый билдер (dsl)
 */
@CoreDslMarker
interface ICoreExecDsl<T> {
	var title: String
	var description: String
	fun on(function: suspend T.() -> Boolean)
	fun except(function: suspend T.(e: Throwable) -> Unit)

	fun build(): ICoreExec<T>
}

/**
 * Билдер (dsl) для цепочек (chain)
 */
@CoreDslMarker
interface ICoreChainDsl<T> : ICoreExecDsl<T> {
	fun add(worker: ICoreExecDsl<T>)
}

/**
 * Билдер (dsl) для рабочих (worker)
 */
@CoreDslMarker
interface ICoreWorkerDsl<T> : ICoreExecDsl<T> {
	fun handle(function: suspend T.() -> Unit)
}

/**
 * Точка входа в dsl построения цепочек.
 * Элементы исполняются последовательно.
 *
 * Пример:
 * ```
 *  chain<SomeContext> {
 *      worker {
 *      }
 *      chain {
 *          worker(...) {
 *          }
 *          worker(...) {
 *          }
 *      }
 *      parallel {
 *         ...
 *      }
 *  }
 * ```
 */
fun <T> rootChain(function: ICoreChainDsl<T>.() -> Unit): ICoreChainDsl<T> = CoreChainDsl<T>().apply(function)


/**
 * Создает цепочку, элементы которой исполняются последовательно.
 */
fun <T> ICoreChainDsl<T>.chain(function: ICoreChainDsl<T>.() -> Unit) {
	add(CoreChainDsl<T>().apply(function))
}

/**
 * Создает цепочку, элементы которой исполняются параллельно. Будьте аккуратны с доступом к контексту -
 * при необходимости используйте синхронизацию
 */
fun <T> ICoreChainDsl<T>.parallel(function: ICoreChainDsl<T>.() -> Unit) {
	add(CoreChainDsl<T>(::executeParallel).apply(function))
}

/**
 * Создает рабочего
 */
fun <T> ICoreChainDsl<T>.worker(function: ICoreWorkerDsl<T>.() -> Unit) {
	add(CoreWorkerDsl<T>().apply(function))
}

/**
 * Создает рабочего с on и except по умолчанию
 */
fun <T> ICoreChainDsl<T>.worker(
	title: String,
	description: String = "",
	blockHandle: T.() -> Unit
) {
	add(CoreWorkerDsl<T>().also {
		it.title = title
		it.description = description
		it.handle(blockHandle)
	})
}