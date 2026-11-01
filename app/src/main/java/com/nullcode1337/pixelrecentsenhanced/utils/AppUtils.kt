package com.nullcode1337.pixelrecentsenhanced.utils

import android.content.pm.PackageManager
import com.nullcode1337.pixelrecentsenhanced.PREnhanced.Companion.appContext
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.LAUNCHER3_PACKAGE
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.PIXEL_LAUNCHER_PACKAGE

object AppUtils {

    val isPixelLauncher = isAppInstalled(PIXEL_LAUNCHER_PACKAGE)
    val isLauncher3 = isAppInstalled(LAUNCHER3_PACKAGE)

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            val pm = appContext.packageManager
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            pm.getApplicationInfo(packageName, 0).enabled
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }
}