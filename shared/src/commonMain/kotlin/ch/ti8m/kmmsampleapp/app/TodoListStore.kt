package ch.ti8m.kmmsampleapp.app

import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TodoListState(
    val todoList: List<TodoItem>,
    val newItem: String,
) : State

sealed class TodoListAction : Action {
    data class Add(val text: String) : TodoListAction()
    data class Update(val text: String) : TodoListAction()
    data class Remove(val index: Int) : TodoListAction()
    data class Error(val error: Exception) : TodoListAction()
}

sealed class TodoListSideEffect : Effect {
    data class Error(val error: Exception) : TodoListSideEffect()
}

class TodoListStore(
    private val repository: TodoListRepository
) : Store<TodoListState, TodoListAction, TodoListSideEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(
        TodoListState(
            todoList = repository.load(),
            newItem = ""
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
                repository.add(
                    TodoItem(
                        text = action.text,
                        created = DateTimeUtil.now()
                    )
                )
                oldState.copy(todoList = repository.load())
            }
            is TodoListAction.Update -> {
                oldState.copy(newItem = action.text)
            }
            is TodoListAction.Remove -> {
                repository.remove(action.index)
                oldState.copy(todoList = repository.load())
            }
            is TodoListAction.Error -> {
                launch { sideEffect.emit(TodoListSideEffect.Error(action.error)) }
                oldState
            }
        }

        if (newState != oldState) {
            Napier.d(tag = "TodoListStore", message = "NewState: $newState")
            state.value = newState
        }
    }
}