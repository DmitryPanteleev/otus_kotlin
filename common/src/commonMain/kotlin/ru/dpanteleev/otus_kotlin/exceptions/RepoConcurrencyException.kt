package ru.dpanteleev.otus_kotlin.exceptions

import ru.dpanteleev.otus_kotlin.models.MgLock

class RepoConcurrencyException(expectedLock: MgLock, actualLock: MgLock?) : RuntimeException(
	"Expected lock is $expectedLock while actual lock in db is $actualLock"
)
