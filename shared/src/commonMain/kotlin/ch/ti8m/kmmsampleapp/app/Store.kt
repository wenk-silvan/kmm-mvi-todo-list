package ch.ti8m.kmmsampleapp.app

import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import kotlinx.serialization.Serializable

typealias Access<S> = () -> S
typealias Change<C> = (C) -> Unit
typealias Observe<S> = ((S) -> Unit) -> Unit
typealias Inject<S> = (S?) -> Unit
typealias AppStore = Store<AppState, AppState.Change>

data class Store<S, C>(
    val state: Access<S>,
    val change: Change<C>,
    val observe: Observe<S>,
    val inject: Inject<S>
)

enum class Screen {
    TODO_LIST,
    TODO_ITEM,
}

@Serializable
data class AppState(
    val todoList: List<TodoItem> = emptyList(),
    val backStack: List<Screen> = listOf(Screen.TODO_LIST),
    val newItemText: String = "",
    val todoItem: TodoItem? = null,
    val error: DomainError? = null,
) {
    fun alter(change: Change) =
        when (change) {
            is Change.By.Adding.Screen -> copy(backStack = backStack + change.screen)
            is Change.By.Adding.Todo -> copy(todoList = todoList + change.todo)
            is Change.By.Removing.TopMostScreen -> copy(backStack = backStack.dropLast(1))
            is Change.By.Removing.Todo -> copy(todoList = todoList - change.todo)
            is Change.By.Updating.TodoItem.Item -> copy(todoItem = change.todo)
            is Change.By.Updating.TodoItem.Text -> copy(todoItem = todoItem?.copy(text = change.text))
            is Change.By.Updating.NewTodoItemName -> copy(newItemText = change.name)
            is Change.By.Updating.TodoList -> copy(todoList = change.todoList)
        }

    sealed class Change {
        sealed class By : Change() {
            sealed class Adding : By() {
                class Screen(val screen: ch.ti8m.kmmsampleapp.app.Screen) : Adding()
                class Todo(val todo: TodoItem) : Adding()
            }

            sealed class Removing : By() {
                data object TopMostScreen : Removing()
                class Todo(val todo: TodoItem) : Removing()
            }

            sealed class Updating : By() {
                sealed class TodoItem : Updating() {
                    class Item(val todo: ch.ti8m.kmmsampleapp.core.entity.TodoItem) : TodoItem()
                    class Text(val text: String) : TodoItem()
                }

                class NewTodoItemName(val name: String) : Updating()
                class TodoList(val todoList: List<ch.ti8m.kmmsampleapp.core.entity.TodoItem>) : Updating()
            }
        }
    }
}
