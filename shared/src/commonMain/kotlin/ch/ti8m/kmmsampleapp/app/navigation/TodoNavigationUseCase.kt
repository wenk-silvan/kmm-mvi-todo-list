package ch.ti8m.kmmsampleapp.app.navigation

import ch.ti8m.kmmsampleapp.app.*
import ch.ti8m.kmmsampleapp.core.entity.TodoItem

class TodoNavigationUseCase(
    private val store: AppStore,
    override val responder: NoResponder
) : UseCase<TodoNavigationUseCase.Request, Nothing> {

    sealed class Request {
        data object GoBack : Request()
        class NavigateToTodoItem(val todo: TodoItem) : Request()
    }

    override fun request(request: Request) {
        when (request) {
            Request.GoBack -> store.change(AppState.Change.By.Removing.TopMostScreen)
            is Request.NavigateToTodoItem -> {
                store.change(AppState.Change.By.Adding.Screen(Screen.TODO_ITEM))
                store.change(AppState.Change.By.Updating.TodoItem.Item(request.todo))
            }
        }
    }
}
