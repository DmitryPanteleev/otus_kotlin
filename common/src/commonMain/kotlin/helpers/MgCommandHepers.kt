package helpers

import Context
import models.Command

fun Context.isUpdatableCommand() =
	this.command in listOf(Command.CREATE, Command.UPDATE, Command.DELETE)