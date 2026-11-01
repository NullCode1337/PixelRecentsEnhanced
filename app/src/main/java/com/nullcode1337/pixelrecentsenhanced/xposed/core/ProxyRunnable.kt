package com.nullcode1337.pixelrecentsenhanced.xposed.core

import android.os.RemoteException
import com.nullcode1337.pixelrecentsenhanced.IRootProviderProxy

fun interface ProxyRunnable {
    @Throws(RemoteException::class)
    fun run(proxy: IRootProviderProxy)
}
