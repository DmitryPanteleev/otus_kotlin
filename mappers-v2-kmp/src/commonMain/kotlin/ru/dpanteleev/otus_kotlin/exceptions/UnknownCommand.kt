package ru.dpanteleev.otus_kotlin.exceptions

import ru.dpanteleev.otus_kotlin.models.Command

class UnknownCommand(command: Command) : Throwable("Неизвестная команда $command")