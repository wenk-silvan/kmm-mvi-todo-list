package ch.ti8m.kmmsampleapp.app.todolist

import ch.ti8m.kmmsampleapp.app.*
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil

fun handleTodoListAdderResponse(handle: MessageHandle): (TodoListUseCase.Response) -> Unit =
    { response ->
        when (response) {
            is TodoListUseCase.Response.Adding -> handle(TodoListMessage.Adding(response.todo, response.state))
            is TodoListUseCase.Response.Removing -> handle(TodoListMessage.Removing(response.todo, response.state))
        }
    }

class TodoListUseCase(
    private val store: AppStore,
    private val repository: TodoListRepository,
    override val responder: (Response) -> Unit
) : UseCase<TodoListUseCase.Request, TodoListUseCase.Response> {

    sealed class Request {
        class ToAdd(val text: String) : Request() {
            fun toTodoItem() = TodoItem(text, DateTimeUtil.now())
        }

        class ToRemove(val todo: TodoItem) : Request()
    }

    sealed class Response {
        class Adding(val todo: TodoItem, val state: ResponseState) : Response()
        class Removing(val todo: TodoItem, val state: ResponseState) : Response()
    }

    override fun request(request: Request) {
        when (request) {
            is Request.ToAdd -> {
                val item = request.toTodoItem()
                repository.add(item)
                store.change(AppState.Change.By.Adding.Todo(item))
                store.change(AppState.Change.By.Updating.NewTodoItemName(""))
                responder(Response.Adding(item, ResponseState.Success))
            }

            is Request.ToRemove -> {
                repository.remove(request.todo.created)
                store.change(AppState.Change.By.Removing.Todo(request.todo))
                responder(Response.Removing(request.todo, ResponseState.Success))
            }
        }
    }
}
