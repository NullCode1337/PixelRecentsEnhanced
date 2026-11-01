package com.nullcode1337.pixelrecentsenhanced.data.provider

import com.crossbowffs.remotepreferences.RemotePreferenceFile
import com.crossbowffs.remotepreferences.RemotePreferenceProvider
import com.nullcode1337.pixelrecentsenhanced.BuildConfig
import com.nullcode1337.pixelrecentsenhanced.data.common.Constants.SHARED_PREFERENCES

class RemotePrefProvider : RemotePreferenceProvider(
    BuildConfig.APPLICATION_ID,
    arrayOf(RemotePreferenceFile(SHARED_PREFERENCES, true))
)