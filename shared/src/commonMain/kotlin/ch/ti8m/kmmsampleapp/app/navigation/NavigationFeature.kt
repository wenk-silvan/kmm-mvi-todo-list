package ch.ti8m.kmmsampleapp.app.navigation

import ch.ti8m.kmmsampleapp.app.*
import ch.ti8m.kmmsampleapp.app.navigation.TodoNavigationUseCase.Request.NavigateToTodoItem
import ch.ti8m.kmmsampleapp.app.todo.TodoItemMessage
import ch.ti8m.kmmsampleapp.core.entity.TodoItem

sealed class NavigationMessage : Message {
    data object GoBack : NavigationMessage()
    class NavigateToTodoItem(val todo: TodoItem) : NavigationMessage()
}

internal fun createNavigationFeature(
    store: AppStore,
    handle: MessageHandle
): MessageHandle {
    val todo = TodoNavigationUseCase(store, NoResponse)

    return { message ->
        if (message is NavigationMessage) {
            when (message) {
                is NavigationMessage.GoBack -> request(todo, TodoNavigationUseCase.Request.GoBack)
                is NavigationMessage.NavigateToTodoItem -> request(todo, NavigateToTodoItem(message.todo))
            }
        } else if (message is TodoItemMessage.SavingItem) {
            request(todo, TodoNavigationUseCase.Request.GoBack)
        }
    }
}
