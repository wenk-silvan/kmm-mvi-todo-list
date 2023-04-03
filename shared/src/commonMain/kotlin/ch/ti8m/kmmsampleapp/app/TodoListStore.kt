package ch.ti8m.kmmsampleapp.app

import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime

data class TodoListState(
    val addItemState: MutableStateFlow<AddItemState>,
    val todoList: List<TodoItem>,
) : State

sealed class TodoListAction : Action {
    data class Remove(val dateTime: LocalDateTime) : TodoListAction()
    object Load : TodoListAction()
}

sealed class TodoListSideEffect : Effect

class TodoListStore(
    private val repository: TodoListRepository,
) : Store<TodoListState, TodoListAction, TodoListSideEffect>,

    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    val onAddItem = { dispatch(TodoListAction.Load) }
    val addItemStore = AddItemStore(repository, onAddItem)

    private val state = MutableStateFlow(
        TodoListState(
            todoList = repository.load(),
            addItemState = addItemStore.state,
        ),
    )
    private val sideEffect = MutableSharedFlow<TodoListSideEffect>()

    override fun observeState(): StateFlow<TodoListState> = state

    override fun observeSideEffect(): Flow<TodoListSideEffect> = sideEffect

    override fun dispatch(action: TodoListAction) {
        Napier.d(tag = "TodoListStore", message = "Action: $action")
        val oldState = state.value

        val newState = when (action) {
            is TodoListAction.Remove -> {
                repository.remove(action.dateTime)
                dispatch(TodoListAction.Load)
                oldState
            }
            is TodoListAction.Load -> {
                oldState.copy(todoList = repository.load())
            }
        }

        if (newState != oldState) {
            Napier.d(tag = "TodoListStore", message = "NewState: $newState")
            state.value = newState
        }
    }
}