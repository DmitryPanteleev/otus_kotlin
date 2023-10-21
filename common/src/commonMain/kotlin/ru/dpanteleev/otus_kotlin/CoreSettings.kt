package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.logging.common.LoggerProvider
import ru.dpanteleev.otus_kotlin.repo.IMgRepository

data class CoreSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repoStub: IMgRepository = IMgRepository.NONE,
    val repoTest: IMgRepository = IMgRepository.NONE,
    val repoProd: IMgRepository = IMgRepository.NONE,
) {
    companion object {
        val NONE = CoreSettings()
    }
}
