package ch.ti8m.kmmsampleapp.app.todolist

import ch.ti8m.kmmsampleapp.app.*
import ch.ti8m.kmmsampleapp.app.todolist.NewTodoListItemUseCase.Request.UpdateText
import ch.ti8m.kmmsampleapp.app.todolist.TodoListUseCase.Request.ToAdd
import ch.ti8m.kmmsampleapp.app.todolist.TodoListUseCase.Request.ToRemove
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository

sealed class TodoListMessage : Message {
    class UpdateNewItemText(val text: String) : TodoListMessage()

    class Add(val text: String) : TodoListMessage()
    class Remove(val todo: TodoItem) : TodoListMessage()

    class Adding(val todo: TodoItem, val responseState: ResponseState) : TodoListMessage()
    class Removing(val todo: TodoItem, val responseState: ResponseState) : TodoListMessage()
}

internal fun createTodoListFeature(
    store: AppStore,
    todoListRepository: TodoListRepository,
    handle: MessageHandle
): MessageHandle {
    val todoList = TodoListUseCase(store, todoListRepository, handleTodoListAdderResponse(handle))
    val newTodoListItem = NewTodoListItemUseCase(store, NoResponse)

    return { message ->
        if (message is TodoListMessage) {
            when (message) {
                is TodoListMessage.UpdateNewItemText -> request(newTodoListItem, UpdateText(message.text))
                is TodoListMessage.Add -> request(todoList, ToAdd(message.text))
                is TodoListMessage.Remove -> request(todoList, ToRemove(message.todo))
                else -> Unit // ignore responses
            }
        }
    }
}
