package com.nullcode1337.pixelrecentsenhanced.data.config

import android.annotation.SuppressLint
import android.content.Context
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceGroup
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.FIXED_RECENTS_BUTTONS_WIDTH
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.FREEFORM_GESTURE
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.FREEFORM_GESTURE_PROGRESS
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.FREEFORM_MODE
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.RECENTS_CLEAR_ALL_BUTTON
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.RECENTS_DISABLE_SELECTION
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.RECENTS_MEMINFO
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.RECENTS_MEMINFO_CHIP
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.RECENTS_MEMINFO_ZRAM
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.XPOSED_HOOK_CHECK
import com.nullcode1337.pixelrecentsenhanced.data.config.RPrefs.getBoolean
import com.nullcode1337.pixelrecentsenhanced.data.config.RPrefs.getSliderFloat
import com.nullcode1337.pixelrecentsenhanced.data.config.RPrefs.getString
import com.nullcode1337.pixelrecentsenhanced.data.config.RPrefs.getStringSet
import com.nullcode1337.pixelrecentsenhanced.utils.AppUtils.isPixelLauncher

object PrefsHelper {

    fun isVisible(key: String?): Boolean {
        return when (key) {
            XPOSED_HOOK_CHECK -> false

            RECENTS_DISABLE_SELECTION -> isPixelLauncher

            FIXED_RECENTS_BUTTONS_WIDTH -> getBoolean(RECENTS_CLEAR_ALL_BUTTON)

            RECENTS_MEMINFO_ZRAM,
            RECENTS_MEMINFO_CHIP -> getBoolean(RECENTS_MEMINFO)

            FREEFORM_GESTURE_PROGRESS,
            FREEFORM_MODE -> getBoolean(FREEFORM_GESTURE)

            else -> true
        }
    }

    fun isEnabled(key: String): Boolean {
        return when (key) {
            else -> true
        }
    }

    @SuppressLint("DefaultLocale")
    fun getSummary(context: Context, key: String): String? {
        when {
            key.endsWith("Slider") -> {
                val value = String.format("%.2f", getSliderFloat(key, 0f))
                return if (value.endsWith(".00")) value.dropLast(3) else value
            }

            key.endsWith("List") -> {
                return getString(key, "")
            }

            key.endsWith("EditText") -> {
                return getString(key, "")
            }

            key.endsWith("MultiSelect") -> {
                return getStringSet(key, emptySet()).toString()
            }

            else -> return when (key) {
                else -> null
            }
        }
    }

    fun setupAllPreferences(group: PreferenceGroup) {
        var i = 0

        while (true) {
            try {
                val thisPreference = group.getPreference(i)

                setupPreference(thisPreference)

                if (thisPreference is PreferenceGroup) {
                    setupAllPreferences(thisPreference)
                    if (thisPreference is PreferenceCategory && thisPreference.isVisible) {
                        thisPreference.isVisible = (0 until thisPreference.preferenceCount)
                            .any { thisPreference.getPreference(it).isVisible }
                    }
                }
            } catch (_: Throwable) {
                break
            }

            i++
        }
    }

    private fun setupPreference(preference: Preference) {
        try {
            val key = preference.key

            preference.isVisible = isVisible(key)
            preference.isEnabled = isEnabled(key)

            getSummary(preference.context, key)?.let {
                preference.summary = it
            }
        } catch (_: Throwable) {
        }
    }
}
