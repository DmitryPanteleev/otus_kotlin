package ru.dpanteleev.otus_kotlin.statemachine

data class SMTransition(
	val state: SMStates,
	val description: String,
)
