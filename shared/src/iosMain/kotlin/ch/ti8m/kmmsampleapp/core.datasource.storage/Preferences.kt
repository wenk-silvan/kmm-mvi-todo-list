package ch.ti8m.kmmsampleapp.core.datasource.storage

import platform.Foundation.NSUserDefaults

actual fun AppContext.remove(key: String) {
    return NSUserDefaults.standardUserDefaults.remove(key)
}

actual fun AppContext.putString(key: String, value: String) {
    NSUserDefaults.standardUserDefaults.setObject(value, key)
}

actual fun AppContext.getAll(): Map<String, *> {
    return NSUserDefaults.standardUserDefaults.boolForKey(key)
}