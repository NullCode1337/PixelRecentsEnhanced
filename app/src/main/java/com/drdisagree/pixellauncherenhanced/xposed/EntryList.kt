package com.drdisagree.pixellauncherenhanced.xposed

import com.drdisagree.pixellauncherenhanced.data.common.Constants.LAUNCHER3_PACKAGE
import com.drdisagree.pixellauncherenhanced.data.common.Constants.PIXEL_LAUNCHER_PACKAGE
import com.drdisagree.pixellauncherenhanced.xposed.mods.AppShortcuts
import com.drdisagree.pixellauncherenhanced.xposed.mods.ClearAllButton
import com.drdisagree.pixellauncherenhanced.xposed.mods.FreeformMod
import com.drdisagree.pixellauncherenhanced.xposed.mods.LauncherUtils
import com.drdisagree.pixellauncherenhanced.xposed.mods.OpacityModifier
import com.drdisagree.pixellauncherenhanced.xposed.mods.RecentsAppLock
import com.drdisagree.pixellauncherenhanced.xposed.mods.RecentsMemInfo
import com.drdisagree.pixellauncherenhanced.xposed.utils.BroadcastHook

object EntryList {

    private val launcherModPacks: List<Class<out ModPack>> = listOf(
        BroadcastHook::class.java,
        LauncherUtils::class.java,
        OpacityModifier::class.java,
        ClearAllButton::class.java,
        RecentsMemInfo::class.java,
        RecentsAppLock::class.java,
        FreeformMod::class.java,
        AppShortcuts::class.java,
    )

    fun getEntries(packageName: String): ArrayList<Class<out ModPack>> {
        val modPacks = ArrayList<Class<out ModPack>>()

        when (packageName) {
            PIXEL_LAUNCHER_PACKAGE,
            LAUNCHER3_PACKAGE -> {
                modPacks.addAll(launcherModPacks)
            }
        }

        return modPacks
    }
}
