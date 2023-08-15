package ch.ti8m.kmmsampleapp.app.todolist

import ch.ti8m.kmmsampleapp.app.AppState
import ch.ti8m.kmmsampleapp.app.AppStore
import ch.ti8m.kmmsampleapp.app.UseCase

class NewTodoListItemUseCase(
    private val store: AppStore, override val responder: (Nothing) -> Unit
) : UseCase<NewTodoListItemUseCase.Request, Nothing> {

    sealed class Request {
        class UpdateText(val name: String) : Request()
    }

    override fun request(request: Request) {
        when (request) {
            is Request.UpdateText -> {
                store.change(AppState.Change.By.Updating.NewTodoItemName(request.name))
            }
        }
    }
}
