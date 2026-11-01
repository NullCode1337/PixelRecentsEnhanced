package com.nullcode1337.pixelrecentsenhanced.xposed.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.crossbowffs.remotepreferences.RemotePreferences
import com.nullcode1337.pixelrecentsenhanced.BuildConfig
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.DEBUG_REPORT_REQUEST
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.PREF_UPDATE_EXCLUSIONS
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.SHARED_PREFERENCES
import com.nullcode1337.pixelrecentsenhanced.xposed.HookEntry
import com.nullcode1337.pixelrecentsenhanced.xposed.mods.toolkit.log

object XPrefs {

    @SuppressLint("StaticFieldLeak")
    lateinit var Xprefs: ExtendedRemotePreferences
    private val listener = OnSharedPreferenceChangeListener { _: SharedPreferences?, key: String? ->
        loadEverything(key)
    }

    val XprefsIsInitialized: Boolean
        get() = ::Xprefs.isInitialized

    fun init(context: Context) {
        Xprefs = ExtendedRemotePreferences(
            context,
            BuildConfig.APPLICATION_ID,
            SHARED_PREFERENCES,
            true
        )
        (Xprefs as RemotePreferences).registerOnSharedPreferenceChangeListener(listener)
    }

    private fun loadEverything(vararg key: String?) {
        if (key.firstOrNull() == DEBUG_REPORT_REQUEST) {
            Xprefs.getString(DEBUG_REPORT_REQUEST, null)?.let { DebugRecorder.respond(it) }
            return
        }
        val name = key.firstOrNull()
        if (name.isNullOrEmpty() ||
            PREF_UPDATE_EXCLUSIONS.any { exclusion -> name == exclusion } ||
            name.startsWith(BootLoopProtector.LOAD_TIME_KEY_KEY) ||
            name.startsWith(BootLoopProtector.PACKAGE_STRIKE_KEY_KEY)
        ) return

        HookEntry.runningMods.forEach { thisMod ->
            try {
                thisMod.updatePrefs(*key.filterNotNull().toTypedArray())
            } catch (throwable: Throwable) {
                log(this@XPrefs, "${thisMod.javaClass.simpleName} -> " + throwable)
            }
        }
    }
}
