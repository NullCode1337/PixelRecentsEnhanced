package com.nullcode1337.pixelrecentsenhanced.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.nullcode1337.pixelrecentsenhanced.R
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.LAUNCHER3_PACKAGE
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.PIXEL_LAUNCHER_PACKAGE
import com.nullcode1337.pixelrecentsenhanced.data.config.RPrefs
import com.nullcode1337.pixelrecentsenhanced.utils.AppUtils.isLauncher3
import com.nullcode1337.pixelrecentsenhanced.utils.AppUtils.isPixelLauncher
import com.nullcode1337.pixelrecentsenhanced.xposed.utils.BootLoopProtector.LOAD_TIME_KEY_KEY
import com.nullcode1337.pixelrecentsenhanced.xposed.utils.BootLoopProtector.PACKAGE_STRIKE_KEY_KEY
import com.topjohnwu.superuser.Shell
import java.util.Calendar

object LauncherUtils {

    fun Context.restartLauncher() {
        Messages.show(this, getString(R.string.restarting_launcher))

        Handler(Looper.getMainLooper()).postDelayed({
            if (isPixelLauncher) {
                resetBootloopProtectorForPackage(PIXEL_LAUNCHER_PACKAGE)
                Shell.cmd("killall $PIXEL_LAUNCHER_PACKAGE").submit()
            } else if (isLauncher3) {
                resetBootloopProtectorForPackage(LAUNCHER3_PACKAGE)
                Shell.cmd("killall $LAUNCHER3_PACKAGE").submit()
            }
        }, 300)
    }

    fun resetBootloopProtectorForPackage(packageName: String) {
        val loadTimeKey = String.format("%s%s", LOAD_TIME_KEY_KEY, packageName)
        val strikeKey = String.format("%s%s", PACKAGE_STRIKE_KEY_KEY, packageName)
        val currentTime = Calendar.getInstance().time.time

        RPrefs.putLong(loadTimeKey, currentTime)
        RPrefs.putInt(strikeKey, 0)
    }
}