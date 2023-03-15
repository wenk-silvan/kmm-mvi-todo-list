package ch.ti8m.kmmsampleapp.core.datasource.storage

import android.content.Context

const val PREFS_NAME = "todo_list_app"

actual fun AppContext.remove(key: String) {
    getPrefsEditor().remove(key).apply()
}

actual fun AppContext.putString(key: String, value: String) {
    getPrefsEditor().putString(key, value).apply()
}

actual fun AppContext.getAll(): Map<String, *> {
    return getPrefs().all
}

private fun AppContext.getPrefs() = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

private fun AppContext.getPrefsEditor() = getPrefs().edit()