package ru.dpanteleev.otus_kotlin.helpers

import ru.dpanteleev.otus_kotlin.Context
import ru.dpanteleev.otus_kotlin.models.Command

fun Context.isUpdatableCommand() =
	this.command in listOf(Command.CREATE, Command.UPDATE, Command.DELETE)