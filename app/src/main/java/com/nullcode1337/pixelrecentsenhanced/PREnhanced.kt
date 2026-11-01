package com.nullcode1337.pixelrecentsenhanced

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors
import java.lang.ref.WeakReference

class PREnhanced : Application() {

    companion object {
        private var instance: PREnhanced? = null
        private var contextReference: WeakReference<Context>? = null

        val appContext: Context
            get() {
                if (contextReference == null || contextReference?.get() == null) {
                    contextReference = WeakReference(
                        instance?.applicationContext ?: getInstance().applicationContext
                    )
                }
                return contextReference!!.get()!!
            }

        private fun getInstance(): PREnhanced {
            if (instance == null) {
                instance = PREnhanced()
            }
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        contextReference = WeakReference(applicationContext)
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}