package com.nullcode1337.pixelrecentsenhanced.xposed.core

import android.content.Context
import android.content.pm.PackageManager
import com.nullcode1337.pixelrecentsenhanced.BuildConfig

abstract class ModPack(private val context: Context) {

    protected val mContext: Context get() = context

    protected val appContext: Context
        get() = try {
            context.createPackageContext(
                BuildConfig.APPLICATION_ID,
                Context.CONTEXT_IGNORE_SECURITY
            )
        } catch (exception: PackageManager.NameNotFoundException) {
            throw RuntimeException(exception)
        }

    abstract fun updatePrefs(vararg key: String)

    abstract fun handleLoadPackage(loadedPackage: LoadedPackage)
}
