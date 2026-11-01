package com.nullcode1337.pixelrecentsenhanced.xposed.mods.toolkit

import java.util.concurrent.ConcurrentHashMap

private val methodPresence = ConcurrentHashMap<List<Any?>, Boolean>()

fun Any?.hasMethod(methodName: String, vararg parameterTypes: Class<*>?): Boolean {
    if (this == null) return false
    val type = this as? Class<*> ?: this::class.java
    val key = listOf(type, methodName, *parameterTypes)
    return methodPresence.getOrPut(key) { type.findsMethod(methodName, parameterTypes) }
}

private fun Class<*>.findsMethod(methodName: String, parameterTypes: Array<out Class<*>?>): Boolean {
    if (parameterTypes.isEmpty()) {
        return declaredMethods.any { it.name == methodName } || methods.any { it.name == methodName }
    }
    return try {
        getDeclaredMethod(methodName, *parameterTypes)
        true
    } catch (_: NoSuchMethodException) {
        try {
            getMethod(methodName, *parameterTypes)
            true
        } catch (_: NoSuchMethodException) {
            false
        }
    }
}
