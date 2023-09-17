package ru.dpanteleev.otus_kotlin.statemachine

import kotlin.time.Duration

data class SMAdSignal(
    val state: SMStates,
    val duration: Duration,
    val views: Int,
)
