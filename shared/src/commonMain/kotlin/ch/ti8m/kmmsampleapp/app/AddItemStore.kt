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

data class AddItemState(
    val newItem: String,
    val error: Boolean,
) : State

sealed class AddItemAction : Action {
    data class Add(val text: String) : AddItemAction()
    data class UpdateNewItem(val text: String) : AddItemAction()
}

sealed class AddItemSideEffect : Effect {
    data class Error(val exception: Exception) : AddItemSideEffect()
}

class AddItemStore(
    private val repository: TodoListRepository,
    private val todoListStore: TodoListStore,
) : Store<AddItemState, AddItemAction, AddItemSideEffect>,
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(
        AddItemState(
            newItem = "",
            error = false,
        ),
    )
    private val sideEffect = MutableSharedFlow<AddItemSideEffect>()

    override fun observeState(): StateFlow<AddItemState> = state

    override fun observeSideEffect(): Flow<AddItemSideEffect> = sideEffect

    override fun dispatch(action: AddItemAction) {
        Napier.d(tag = "TodoListStore", message = "Action: $action")
        val oldState = state.value

        val newState = when (action) {
            is AddItemAction.Add -> {
                if (action.text == "") {
                    launch {
                        sideEffect.emit(AddItemSideEffect.Error(IllegalArgumentException("Can't add empty item")))
                    }
                    oldState
                } else {
                    repository.add(TodoItem(text = action.text, created = DateTimeUtil.now()))
                    todoListStore.dispatch(TodoListAction.Load)
                    oldState.copy(newItem = "")
                }
            }
            is AddItemAction.UpdateNewItem -> oldState.copy(newItem = action.text)
        }

        if (newState != oldState) {
            Napier.d(tag = "AddItemStore", message = "NewState: $newState")
            state.value = newState
        }
    }
}