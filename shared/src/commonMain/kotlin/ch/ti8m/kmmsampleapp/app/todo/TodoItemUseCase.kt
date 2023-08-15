package ch.ti8m.kmmsampleapp.app.todo

import ch.ti8m.kmmsampleapp.app.*
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository

fun handleTodoItemResponse(handle: MessageHandle): (TodoItemUseCase.Response) -> Unit =
    { response ->
        when (response) {
            is TodoItemUseCase.Response.Saving -> handle(TodoItemMessage.SavingItem(response.todo, response.state))
        }
    }

class TodoItemUseCase(
    private val store: AppStore,
    private val repository: TodoListRepository,
    override val responder: (Response) -> Unit
) : UseCase<TodoItemUseCase.Request, TodoItemUseCase.Response> {

    sealed class Request {
        class UpdateItemText(val text: String) : Request()
        data object SaveItem : Request()
    }

    sealed class Response {
        class Saving(val todo: TodoItem, val state: ResponseState) : Response()
    }

    override fun request(request: Request) {
        when (request) {
            is Request.UpdateItemText -> store.change(AppState.Change.By.Updating.TodoItem.Text(request.text))

            is Request.SaveItem -> {
                val state = store.state()
                val item = state.todoItem!!
                repository.update(item)
                val items = state.todoList.map { if (it.created == item.created) item else it }
                store.change(AppState.Change.By.Updating.TodoList(items))
                responder(Response.Saving(item, ResponseState.Success))
            }
        }
    }
}
