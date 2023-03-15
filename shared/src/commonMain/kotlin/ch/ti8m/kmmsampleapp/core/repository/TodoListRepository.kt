package ch.ti8m.kmmsampleapp.core.repository

import ch.ti8m.kmmsampleapp.core.datasource.storage.TodoListPreferences
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil
import kotlinx.datetime.LocalDateTime

class TodoListRepository(
    private val preferences: TodoListPreferences,
) {

    fun add(item: TodoItem) {
        preferences.put(
            key = DateTimeUtil.toString(item.created),
            value = item.text,
        )
    }

    fun remove(dateTime: LocalDateTime) {
        preferences.remove(DateTimeUtil.toString(dateTime))
    }

    fun load(): List<TodoItem> = preferences
        .getAll()
        .map {
            TodoItem(
                text = it.value.substringAfter('='),
                created = DateTimeUtil.fromString(it.key),
            )
        }.toList()

    companion object
}