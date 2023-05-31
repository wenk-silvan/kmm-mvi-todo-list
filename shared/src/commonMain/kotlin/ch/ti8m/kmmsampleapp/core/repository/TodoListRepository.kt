package ch.ti8m.kmmsampleapp.core.repository

import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.datetime.LocalDateTime

class TodoListRepository(
    private val settings: Settings,
) {
    fun add(item: TodoItem) {
        settings.putString(
            key = SETTINGS_KEY + DateTimeUtil.toString(item.created),
            value = item.text,
        )
    }
    fun update(text: String, created: LocalDateTime) {
        remove(created)
        add(TodoItem(text = text, created))
    }

    fun remove(dateTime: LocalDateTime) {
        settings.remove(SETTINGS_KEY + DateTimeUtil.toString(dateTime))
    }

    fun load(): List<TodoItem> {
        return try {
            settings.keys
                .filter { key -> key.startsWith(SETTINGS_KEY) }
                .map { key ->
                    TodoItem(
                        text = settings[key]!!,
                        created = DateTimeUtil.fromString(key.substringAfter(SETTINGS_KEY)),
                    )
                }
                .sortedByDescending { item -> item.created }
                .toList()
        } catch(ex: Exception) {
            listOf(TodoItem(ex.toString(), DateTimeUtil.now()))
        }
    }

    companion object {
        private const val SETTINGS_KEY = "TODOLIST"
    }
}