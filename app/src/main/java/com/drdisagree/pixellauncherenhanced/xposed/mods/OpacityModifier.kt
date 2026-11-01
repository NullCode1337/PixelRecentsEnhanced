package com.drdisagree.pixellauncherenhanced.xposed.mods

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.drdisagree.pixellauncherenhanced.data.common.Constants.DISABLE_RECENTS_LIVE_TILE
import com.drdisagree.pixellauncherenhanced.data.common.Constants.RECENTS_BACKGROUND_OPACITY
import com.drdisagree.pixellauncherenhanced.xposed.core.LoadedPackage
import com.drdisagree.pixellauncherenhanced.xposed.core.ModPack
import com.drdisagree.pixellauncherenhanced.xposed.hook.findClass
import com.drdisagree.pixellauncherenhanced.xposed.hook.hookMethod
import com.drdisagree.pixellauncherenhanced.xposed.prefs.XPrefs.Xprefs
import com.drdisagree.pixellauncherenhanced.xposed.reflection.callMethod
import com.drdisagree.pixellauncherenhanced.xposed.reflection.callMethodSilently
import com.drdisagree.pixellauncherenhanced.xposed.reflection.getField
import com.drdisagree.pixellauncherenhanced.xposed.reflection.getFieldSilently

class OpacityModifier(context: Context) : ModPack(context) {

    private var recentsBackgroundOpacity: Int = -1
    private var disableRecentsLiveTile: Boolean = false

    override fun updatePrefs(vararg key: String) {
        Xprefs.apply {
            recentsBackgroundOpacity = getSliderInt(RECENTS_BACKGROUND_OPACITY, -1) * 255 / 100
            disableRecentsLiveTile = getBoolean(DISABLE_RECENTS_LIVE_TILE, false)
        }
    }

    override fun handleLoadPackage(loadedPackage: LoadedPackage) {
        val overviewStateClass = findClass("com.android.launcher3.uioverrides.states.OverviewState")
        val quickSwitchStateClass =
            findClass("com.android.launcher3.uioverrides.states.QuickSwitchState")
        val recentsStateClass = findClass("com.android.quickstep.fallback.RecentsState")
        val hintStateClass = findClass(
            "com.android.launcher3.states.HintState",
            "com.android.launcher3.uioverrides.states.HintState"
        )
        val scrimColorsClass = findClass(
            "com.android.launcher3.views.ScrimColors",
            suppressError = true
        )

        overviewStateClass
            .hookMethod("getWorkspaceScrimColor")
            .runAfter { param ->
                if (recentsBackgroundOpacity < 0) return@runAfter

                param.result = applyOpacity(param.result, scrimColorsClass)
            }

        quickSwitchStateClass
            .hookMethod("getWorkspaceScrimColor")
            .runAfter { param ->
                if (recentsBackgroundOpacity < 0) return@runAfter

                val launcher = param.args[0]
                val deviceProfile = launcher.callMethod("getDeviceProfile")
                val isTaskbarPresentInApps =
                    deviceProfile.callMethodSilently("isTaskbarPresentInApps") as? Boolean
                        ?: deviceProfile.getField("isTaskbarPresentInApps") as Boolean
                val currentResult = param.result as Int

                if (currentResult != Color.TRANSPARENT && !isTaskbarPresentInApps) {
                    param.result = applyOpacity(param.result, scrimColorsClass)
                }
            }

        recentsStateClass
            .hookMethod("getScrimColor")
            .runAfter { param ->
                if (recentsBackgroundOpacity < 0) return@runAfter

                param.result = applyOpacity(param.result, scrimColorsClass)
            }

        hintStateClass
            .hookMethod("getWorkspaceScrimColor")
            .runAfter { param ->
                if (recentsBackgroundOpacity < 0) return@runAfter

                param.result = applyOpacity(param.result, scrimColorsClass)
            }

        findClass("com.android.quickstep.views.RecentsView")
            .hookMethod("onGestureAnimationEnd")
            .runAfter { param ->
                if (!disableRecentsLiveTile) return@runAfter
                if (param.thisObject.getFieldSilently("mEnableDrawingLiveTile") != true) return@runAfter

                param.thisObject.callMethod(
                    "switchToScreenshot",
                    Runnable {
                        param.thisObject.callMethod(
                            "finishRecentsAnimation",
                            true /* toRecents */,
                            false /* shouldPip */,
                            null
                        )
                    }
                )
            }
    }

    private fun applyOpacity(result: Any?, scrimColorsClass: Class<*>?): Any {
        return if (result is Int) {
            ColorUtils.setAlphaComponent(result, recentsBackgroundOpacity)
        } else {
            getScrimColors(result, scrimColorsClass!!, recentsBackgroundOpacity)
        }
    }

    private fun getScrimColors(
        scrimColors: Any?,
        scrimColorsClass: Class<*>,
        backgroundOpacity: Int
    ): Any {
        val backgroundColor = scrimColors.callMethodSilently("getBackgroundColor") as? Int
            ?: scrimColors.getField("backgroundColor") as Int
        val foregroundColor = scrimColors.callMethodSilently("getForegroundColor") as? Int
            ?: scrimColors.getField("foregroundColor") as Int

        return scrimColorsClass
            .getDeclaredConstructor(
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            .newInstance(
                ColorUtils.setAlphaComponent(backgroundColor, backgroundOpacity),
                foregroundColor
            )
    }
}
