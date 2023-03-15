package ch.ti8m.kmmsampleapp.app

import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

data class TodoListState(
    val todoList: List<TodoItem>,
    val newItem: String,
) : State

sealed class TodoListAction : Action {
    data class Add(val text: String) : TodoListAction()
    data class UpdateNewItem(val text: String) : TodoListAction()
    data class Remove(val dateTime: LocalDateTime) : TodoListAction()
    data class Error(val exception: Exception) : TodoListAction()
}

sealed class TodoListSideEffect : Effect {
    data class Error(val exception: Exception) : TodoListSideEffect()
}

class TodoListStore(
    private val repository: TodoListRepository,
) : Store<TodoListState, TodoListAction, TodoListSideEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(
        TodoListState(
            todoList = repository.load(),
            newItem = "",
        ),
    )
    private val sideEffect = MutableSharedFlow<TodoListSideEffect>()

    override fun observeState(): StateFlow<TodoListState> = state

    override fun observeSideEffect(): Flow<TodoListSideEffect> = sideEffect

    override fun dispatch(action: TodoListAction) {
        Napier.d(tag = "TodoListStore", message = "Action: $action")
        val oldState = state.value

        val newState = when (action) {
            is TodoListAction.Add -> {
                if (action.text == "") {
                    launch {
                        sideEffect.emit(TodoListSideEffect.Error(IllegalArgumentException("Can't add empty item")))
                    }
                    oldState
                } else {
                    repository.add(
                        TodoItem(
                            text = action.text,
                            created = DateTimeUtil.now()
                        )
                    )
                    oldState.copy(todoList = repository.load())
                }
            }
            is TodoListAction.UpdateNewItem -> {
                oldState.copy(newItem = action.text)
            }
            is TodoListAction.Remove -> {
                repository.remove(action.dateTime)
                oldState.copy(todoList = repository.load())
            }
            is TodoListAction.Error -> {
                Napier.e(
                    tag = "TodoListStore",
                    throwable = action.exception,
                    message = action.exception.message ?: "No message"
                )
                launch { sideEffect.emit(TodoListSideEffect.Error(action.exception)) }
                oldState
            }
        }

        if (newState != oldState) {
            Napier.d(tag = "TodoListStore", message = "NewState: $newState")
            state.value = newState
        }
    }
}