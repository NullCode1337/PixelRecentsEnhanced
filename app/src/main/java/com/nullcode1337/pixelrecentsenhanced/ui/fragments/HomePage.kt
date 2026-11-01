package com.nullcode1337.pixelrecentsenhanced.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.preference.Preference
import com.nullcode1337.pixelrecentsenhanced.R
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.RESTART_LAUNCHER
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.XPOSED_HOOK_CHECK
import com.nullcode1337.pixelrecentsenhanced.ui.base.ControlledPreferenceFragmentCompat
import com.nullcode1337.pixelrecentsenhanced.ui.preferences.HookCheckPreference
import com.nullcode1337.pixelrecentsenhanced.utils.LauncherUtils.restartLauncher

class HomePage : ControlledPreferenceFragmentCompat() {

    private var hookCheckPreference: HookCheckPreference? = null

    override val title: String
        get() = getString(R.string.fragment_recents_title)

    override val backButtonEnabled: Boolean
        get() = false

    override val layoutResource: Int
        get() = R.xml.home_page

    override val hasMenu: Boolean
        get() = false

    override val themeResource: Int
        get() = R.style.PrefsThemeCollapsingToolbar

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        findPreference<HookCheckPreference>(XPOSED_HOOK_CHECK)?.apply {
            hookCheckPreference = this
            initializeHookCheck()
        }

        findPreference<Preference>(RESTART_LAUNCHER)?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                context?.restartLauncher()
                true
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onResume() {
        super.onResume()

        HookCheckPreference.isHooked = false
        hookCheckPreference?.initializeHookCheck()
    }
}
