package ru.dpanteleev.otus_kotlin.exceptions

import kotlin.reflect.KClass

class UnknownClass(clazz: KClass<*>) : RuntimeException("неизвестный класс $clazz")