package ch.ti8m.kmmsampleapp.core.datasource.storage

class TodoListPreferences(
    private val context: AppContext,
) {

    fun remove(key: String) {
        context.remove(key)
    }

    fun put(key: String, value: String) {
        context.putString(key, value)
    }

    fun getAll(): Map<String, String> = context.getAll().mapValues { v -> v.toString() }
}