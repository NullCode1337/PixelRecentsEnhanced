package com.nullcode1337.pixelrecentsenhanced.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nullcode1337.pixelrecentsenhanced.data.module.ModuleServiceManager

class ModuleUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) ModuleServiceManager.start()
    }
}
