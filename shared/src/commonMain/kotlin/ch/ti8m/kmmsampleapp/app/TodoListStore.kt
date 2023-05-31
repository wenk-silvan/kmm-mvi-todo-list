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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

data class TodoListState(
    val todoList: List<TodoItem>,
    val newItem: String,
    val editItem: TodoItem,
    val error: Boolean,
) : State

sealed class TodoListAction : Action {
    data class Add(val text: String) : TodoListAction()
    data class UpdateNewItem(val text: String) : TodoListAction()
    data class Remove(val dateTime: LocalDateTime) : TodoListAction()
    data class UpdateEditItemText(val text: String) : TodoListAction()
    data class ShowItemDetail(val item: TodoItem) : TodoListAction()
    object SaveEditItem : TodoListAction()
}

sealed class TodoListSideEffect : Effect {
    data class Error(val exception: Exception) : TodoListSideEffect()
    object NavigateToItemDetailScreen : TodoListSideEffect()
    object NavigateToItemListScreen : TodoListSideEffect()
}

class TodoListStore(
    private val repository: TodoListRepository,
) : Store<TodoListState, TodoListAction, TodoListSideEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(
        TodoListState(
            todoList = repository.load(),
            newItem = "",
            editItem = TodoItem(text = "", created = DateTimeUtil.now()),
            error = false,
        ),
    )
    private val sideEffect = MutableSharedFlow<TodoListSideEffect>()

    fun onStateChange(newState: ((TodoListState) -> Unit)) {
        state.onEach {
            newState.invoke(it)
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    fun onSideEffectEmitted(newSideEffect: ((TodoListSideEffect) -> Unit)) {
        sideEffect.onEach {
            newSideEffect.invoke(it)
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

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
                    repository.add(TodoItem(text = action.text, created = DateTimeUtil.now()))
                    oldState.copy(todoList = repository.load(), newItem = "")
                }
            }
            is TodoListAction.UpdateNewItem -> {
                oldState.copy(newItem = action.text)
            }
            is TodoListAction.Remove -> {
                repository.remove(action.dateTime)
                oldState.copy(todoList = repository.load())
            }
            is TodoListAction.UpdateEditItemText -> {
                oldState.copy(editItem = oldState.editItem.copy(text = action.text))
            }
            is TodoListAction.ShowItemDetail -> {
                launch {
                    sideEffect.emit(TodoListSideEffect.NavigateToItemDetailScreen)
                }
                oldState.copy(editItem = action.item)
            }
            is TodoListAction.SaveEditItem -> {
                if (oldState.editItem.text == "") {
                    launch {
                        sideEffect.emit(TodoListSideEffect.Error(IllegalArgumentException("Can't add empty item")))
                    }
                    oldState
                } else {
                    launch {
                        sideEffect.emit(TodoListSideEffect.NavigateToItemListScreen)
                    }
                    repository.update(text = oldState.editItem.text, created = oldState.editItem.created)
                    oldState.copy(todoList = repository.load())
                }
            }
        }

        if (newState != oldState) {
            Napier.d(tag = "TodoListStore", message = "NewState: $newState")
            state.value = newState
        }
    }
}