package com.nullcode1337.pixelrecentsenhanced.xposed

import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.LAUNCHER3_PACKAGE
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.PIXEL_LAUNCHER_PACKAGE
import com.nullcode1337.pixelrecentsenhanced.xposed.mods.AppShortcuts
import com.nullcode1337.pixelrecentsenhanced.xposed.mods.ClearAllButton
import com.nullcode1337.pixelrecentsenhanced.xposed.mods.FreeformMod
import com.nullcode1337.pixelrecentsenhanced.xposed.mods.LauncherUtils
import com.nullcode1337.pixelrecentsenhanced.xposed.mods.OpacityModifier
import com.nullcode1337.pixelrecentsenhanced.xposed.mods.RecentsAppLock
import com.nullcode1337.pixelrecentsenhanced.xposed.mods.RecentsMemInfo
import com.nullcode1337.pixelrecentsenhanced.xposed.utils.BroadcastHook

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
