package com.drdisagree.pixellauncherenhanced.data.common

import com.drdisagree.pixellauncherenhanced.BuildConfig
import com.drdisagree.pixellauncherenhanced.xposed.utils.BootLoopProtector

object Constants {

    // Shared Preference File
    const val SHARED_PREFERENCES = "${BuildConfig.APPLICATION_ID}_preferences"

    // System packages
    const val FRAMEWORK_PACKAGE = "android"
    const val PIXEL_LAUNCHER_PACKAGE = "com.google.android.apps.nexuslauncher"
    const val LAUNCHER3_PACKAGE = "com.android.launcher3"

    // Preferences
    const val VIBRATE_UI = "vibrate_ui"
    const val XPOSED_HOOK_CHECK = "xposed_hook_check"
    const val RESTART_LAUNCHER = "xposed_restartlauncher"
    const val ACTION_HOOK_CHECK_REQUEST = "${BuildConfig.APPLICATION_ID}.ACTION_HOOK_CHECK_REQUEST"
    const val ACTION_HOOK_CHECK_RESULT = "${BuildConfig.APPLICATION_ID}.ACTION_HOOK_CHECK_RESULT"
    const val DEBUG_REPORT_REQUEST = "xposed_debugreportrequest"
    const val DEBUG_REPORT_RESPONSE = "xposed_debugreportresponse"
    const val RECENTS_BACKGROUND_OPACITY = "xposed_recentsbackgroundopacity"
    const val DISABLE_RECENTS_LIVE_TILE = "xposed_disablerecentslivetile"
    const val RECENTS_CLEAR_ALL_BUTTON = "xposed_recentsclearallbutton"
    const val FIXED_RECENTS_BUTTONS_WIDTH = "xposed_fixedrecentsbuttonswidth"
    const val RECENTS_REMOVE_SCREENSHOT_BUTTON = "xposed_recentsremovescreenshotbutton"
    const val RECENTS_DISABLE_SELECTION = "xposed_recentsdisableselection"
    const val RECENTS_MEMINFO = "xposed_recentsmeminfo"
    const val RECENTS_MEMINFO_ZRAM = "xposed_recentsmeminfozram"
    const val RECENTS_MEMINFO_CHIP = "xposed_recentsmeminfochip"
    const val RECENTS_KILL_APP = "xposed_recentskillapp"
    const val RECENTS_UNINSTALL_APP = "xposed_recentsuninstallapp"
    const val RECENTS_LOCK_APP = "xposed_recentslockapp"
    const val FREEFORM_GESTURE = "xposed_startfreeformbygesture"
    const val FREEFORM_MODE = "xposed_freeform_mode"
    const val FREEFORM_GESTURE_PROGRESS = "xposed_startfreeformprogress"

    val PREF_UPDATE_EXCLUSIONS = listOf(
        BootLoopProtector.LOAD_TIME_KEY_KEY,
        BootLoopProtector.PACKAGE_STRIKE_KEY_KEY,
        DEBUG_REPORT_REQUEST,
        DEBUG_REPORT_RESPONSE,
    )
}
