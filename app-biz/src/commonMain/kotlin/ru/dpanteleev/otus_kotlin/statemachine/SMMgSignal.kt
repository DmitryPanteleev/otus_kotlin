package ru.dpanteleev.otus_kotlin.statemachine

import kotlin.time.Duration

data class SMMgSignal(
    val state: SMStates,
    val duration: Duration,
    val views: Int,
)
