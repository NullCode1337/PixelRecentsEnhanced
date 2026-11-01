package com.nullcode1337.pixelrecentsenhanced.xposed.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.ACTION_HOOK_CHECK_REQUEST
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.ACTION_HOOK_CHECK_RESULT
import com.nullcode1337.pixelrecentsenhanced.xposed.ModPack
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class BroadcastHook(context: Context) : ModPack(context) {

    private var broadcastRegistered = false

    override fun updatePrefs(vararg key: String) {}

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun handleLoadPackage(loadPackageParam: LoadPackageParam) {
        if (broadcastRegistered) return

        broadcastRegistered = true

        val intentFilter = IntentFilter().apply {
            addAction(ACTION_HOOK_CHECK_REQUEST)
        }

        val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ACTION_HOOK_CHECK_REQUEST) {
                    returnHookResult()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mContext.registerReceiver(
                broadcastReceiver,
                intentFilter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            mContext.registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    private fun returnHookResult() {
        Thread {
            mContext.sendBroadcast(
                Intent()
                    .setAction(ACTION_HOOK_CHECK_RESULT)
                    .addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
            )
        }.start()
    }
}
