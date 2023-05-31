package ch.ti8m.kmmsampleapp

import ch.ti8m.kmmsampleapp.app.TodoListStore
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository

class AppModule {
    private val repository by lazy {
        TodoListRepository.Companion.create(false)
    }
    val store by lazy {
        TodoListStore(repository)
    }
}