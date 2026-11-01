package com.nullcode1337.pixelrecentsenhanced.xposed.core

data class LoadedPackage(
    val packageName: String,
    val classLoader: ClassLoader,
    val processName: String
)
