package ch.ti8m.kmmsampleapp.app

import ch.ti8m.kmmsampleapp.app.navigation.createNavigationFeature
import ch.ti8m.kmmsampleapp.app.todo.createTodoItemFeature
import ch.ti8m.kmmsampleapp.app.todolist.createTodoListFeature
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import kotlinx.serialization.Serializable

interface Message

typealias MessageHandle = (Message) -> Unit

internal interface UseCase<Request, Response> {

    val responder: (Response) -> Unit

    fun request(request: Request)
}

internal fun <Request, Response> request(useCase: UseCase<Request, Response>, request: Request) =
    useCase.request(request)

internal val NoResponse = { _: Nothing -> }
typealias NoResponder = (Nothing) -> Unit

sealed class ResponseState {
    data object Success : ResponseState()
    class Error(error: DomainError) : ResponseState()
}

@Serializable
sealed class DomainError {
    data object NoInternet : DomainError()
}

fun createAppDomain(
    store: AppStore,
    todoListRepository: TodoListRepository
): MessageHandle {
    var handles = listOf<MessageHandle>()
    fun handle(message: Message) {
        handles.forEach { handle -> handle(message) }
    }
    handles = createFeatures(store, todoListRepository, ::handle)
    return ::handle
}

private fun createFeatures(
    store: AppStore,
    todoListRepository: TodoListRepository,
    handle: MessageHandle
) = listOf(
    createLoggingFeature(store),
    createNavigationFeature(store, handle),
    createTodoListFeature(store, todoListRepository, handle),
    createTodoItemFeature(store, todoListRepository, handle)
)
