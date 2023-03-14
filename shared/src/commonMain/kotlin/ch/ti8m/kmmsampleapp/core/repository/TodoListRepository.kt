package ch.ti8m.kmmsampleapp.core.repository

import ch.ti8m.kmmsampleapp.core.entity.TodoItem

class TodoListRepository {
    private var todoList: List<TodoItem> = listOf()

    fun add(item: TodoItem) {
        val newList = todoList.toMutableList()
        newList.add(item)
        todoList = newList
    }

    fun remove(index: Int) {
        val newList = todoList.toMutableList()
        newList.removeAt(index)
        todoList = newList
    }

    fun load() = todoList

    companion object
}