package exceptions

import kotlin.reflect.KClass

class UnknownClass(clazz: KClass<*>) : RuntimeException("неизвестный класс $clazz")