package com.nullcode1337.pixelrecentsenhanced.utils

import android.os.Handler
import android.os.Looper
import com.nullcode1337.pixelrecentsenhanced.BuildConfig
import com.topjohnwu.superuser.Shell

object RootShell {

    private val mainHandler = Handler(Looper.getMainLooper())

    fun init() {
        Shell.enableVerboseLogging = BuildConfig.DEBUG
        @Suppress("DEPRECATION")
        if (Shell.getCachedShell() == null) {
            Shell.setDefaultBuilder(
                Shell.Builder.create()
                    .setFlags(Shell.FLAG_MOUNT_MASTER or Shell.FLAG_REDIRECT_STDERR)
                    .setTimeout(20)
            )
        }
    }

    fun isRootShell(): Boolean {
        Shell.getCachedShell()?.takeIf { !it.isRoot }?.close()
        return runCatching { Shell.getShell().isRoot }.getOrDefault(false)
    }

    fun checkRoot(callback: (Boolean) -> Unit) {
        init()
        Shell.EXECUTOR.execute {
            val isRooted = isRootShell()
            mainHandler.post { callback(isRooted) }
        }
    }
}
