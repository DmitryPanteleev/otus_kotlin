package ru.otus.otuskotlin.marketplace.core

/**
 * Блок кода, который обрабатывает контекст. Имеет имя и описание
 */
interface ICoreExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}
