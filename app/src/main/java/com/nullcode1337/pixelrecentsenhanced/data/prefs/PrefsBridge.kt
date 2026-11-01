package com.nullcode1337.pixelrecentsenhanced.data.prefs

import android.net.Uri
import android.os.Bundle
import com.nullcode1337.pixelrecentsenhanced.BuildConfig

object PrefsBridge {

    const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.prefs"
    const val METHOD_WRITE = "write"
    const val METHOD_SYNC = "sync"
    const val METHOD_READ = "read"
    const val CHANGES_PATH = "changed"

    private const val KEY_VALUES = "values"
    private const val KEY_REMOVED = "removed"

    val uri: Uri = Uri.parse("content://$AUTHORITY")

    val changesUri: Uri = uri.buildUpon().appendPath(CHANGES_PATH).build()

    fun changeUri(key: String): Uri = changesUri.buildUpon().appendPath(key).build()

    fun changedKey(uri: Uri?): String? = uri?.pathSegments?.takeIf { it.firstOrNull() == CHANGES_PATH }?.getOrNull(1)

    fun encode(values: Map<String, Any?>, removed: Collection<String>): Bundle {
        val bundle = Bundle()
        bundle.putBundle(KEY_VALUES, Bundle().apply {
            values.forEach { (key, value) ->
                when (value) {
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is String -> putString(key, value)
                    is Set<*> -> putStringArrayList(key, ArrayList(value.filterIsInstance<String>()))
                }
            }
        })
        bundle.putStringArrayList(KEY_REMOVED, ArrayList(removed))
        return bundle
    }

    fun decodeValues(bundle: Bundle): Map<String, Any?> {
        val values = bundle.getBundle(KEY_VALUES) ?: return emptyMap()
        return values.keySet().associateWith { key ->
            @Suppress("DEPRECATION")
            when (val value = values.get(key)) {
                is ArrayList<*> -> value.filterIsInstance<String>().toSet()
                else -> value
            }
        }
    }

    fun decodeRemoved(bundle: Bundle): List<String> = bundle.getStringArrayList(KEY_REMOVED).orEmpty()
}
