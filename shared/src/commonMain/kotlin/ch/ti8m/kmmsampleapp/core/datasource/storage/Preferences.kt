package ch.ti8m.kmmsampleapp.core.datasource.storage

expect fun AppContext.remove(key: String)

expect fun AppContext.putString(key: String, value: String)

expect fun AppContext.getAll(): Map<String, *>