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
            key = DateTimeUtil.toString(item.created),
            value = item.text,
        )
    }

    fun remove(dateTime: LocalDateTime) {
        settings.remove(DateTimeUtil.toString(dateTime))
    }

    fun load(): List<TodoItem> = settings.keys
        .map { key ->
            TodoItem(
                text = settings.get<String>(key)!!.substringAfter('='),
                created = DateTimeUtil.fromString(key),
            )
        }.toList()

    companion object
}