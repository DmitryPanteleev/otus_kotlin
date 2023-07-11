package exceptions

import models.Command

class UnknownCommand(command: Command) : Throwable("Неизвестная команда $command")