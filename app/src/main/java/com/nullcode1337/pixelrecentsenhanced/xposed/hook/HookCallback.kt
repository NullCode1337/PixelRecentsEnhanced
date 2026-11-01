package com.nullcode1337.pixelrecentsenhanced.xposed.hook

import com.nullcode1337.pixelrecentsenhanced.xposed.mods.toolkit.log
import com.nullcode1337.pixelrecentsenhanced.xposed.utils.DebugRecorder
import io.github.libxposed.api.XposedInterface

internal class HookCallback(
    private val record: DebugRecorder.HookRecord,
    private val before: ((HookParam) -> Unit)? = null,
    private val after: ((HookParam) -> Unit)? = null
) : XposedInterface.Hooker {

    override fun intercept(chain: XposedInterface.Chain): Any? {
        DebugRecorder.fired(record)
        val param = HookParam(chain)
        before?.let { runSafely(param, it) }
        if (!param.shouldSkipOriginal()) param.proceed()
        after?.let { runSafely(param, it) }
        return param.outcome()
    }

    private fun runSafely(param: HookParam, callback: (HookParam) -> Unit) {
        try {
            callback(param)
        } catch (throwable: Throwable) {
            log(record.owner ?: TAG, throwable)
        }
    }

    private companion object {
        const val TAG = "HookCallback"
    }
}
