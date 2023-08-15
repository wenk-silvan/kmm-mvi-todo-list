package ch.ti8m.kmmsampleapp.app.todo

import ch.ti8m.kmmsampleapp.app.*
import ch.ti8m.kmmsampleapp.app.todo.TodoItemUseCase.Request.SaveItem
import ch.ti8m.kmmsampleapp.app.todo.TodoItemUseCase.Request.UpdateItemText
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository

sealed class TodoItemMessage : Message {
    class UpdateItemText(val text: String) : TodoItemMessage()
    class SaveItem() : TodoItemMessage()
    class SavingItem(val todo: TodoItem, val responseState: ResponseState) : TodoItemMessage()
}

internal fun createTodoItemFeature(
    store: AppStore,
    todoListRepository: TodoListRepository,
    handle: MessageHandle
): MessageHandle {
    val todoItem = TodoItemUseCase(store, todoListRepository, handleTodoItemResponse(handle))

    return { message ->
        if (message is TodoItemMessage) {
            when (message) {
                is TodoItemMessage.UpdateItemText -> request(todoItem, UpdateItemText(message.text))
                is TodoItemMessage.SaveItem -> request(todoItem, SaveItem)
                else -> Unit // ignore responses
            }
        }
    }
}
