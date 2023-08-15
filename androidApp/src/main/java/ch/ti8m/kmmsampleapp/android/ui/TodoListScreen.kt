package ch.ti8m.kmmsampleapp.android.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.ti8m.kmmsampleapp.app.AppState
import ch.ti8m.kmmsampleapp.app.AppStore
import ch.ti8m.kmmsampleapp.app.MessageHandle
import ch.ti8m.kmmsampleapp.app.navigation.NavigationMessage
import ch.ti8m.kmmsampleapp.app.todolist.TodoListMessage
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil

@Composable
fun AppStore.getState(): AppState {
    var state by remember { mutableStateOf(state()) }
    LaunchedEffect(this) {
        observe { state = it }
    }
    return state
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoListScreen(store: AppStore, messageHandle: MessageHandle) {
    TodoListContent(
        store.getState(),
        onNewItemTextChanged = { messageHandle(TodoListMessage.UpdateNewItemText(it)) },
        onAddNewItemClicked = { messageHandle(TodoListMessage.Add(it)) },
        onItemDetailClicked = { messageHandle(NavigationMessage.NavigateToTodoItem(it)) },
        onItemDeleteClicked = { messageHandle(TodoListMessage.Remove(it)) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoListContent(
    state: AppState,
    onNewItemTextChanged: (String) -> Unit = {},
    onAddNewItemClicked: (String) -> Unit = {},
    onItemDetailClicked: (TodoItem) -> Unit = {},
    onItemDeleteClicked: (TodoItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Your list", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.weight(1f),
                value = state.newItemText,
                onValueChange = onNewItemTextChanged,
                singleLine = true,
            )
            IconButton(onClick = { onAddNewItemClicked(state.newItemText) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adds todo item")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(
                items = state.todoList,
                key = { index, _ -> index },
            ) { _, todoItem ->
                TodoListItem(
                    modifier = Modifier.animateItemPlacement(),
                    item = todoItem,
                    onItemDetailClicked = onItemDetailClicked,
                    onRemove = onItemDeleteClicked,
                )
            }
        }
    }
}

@Composable
private fun TodoListItem(
    modifier: Modifier = Modifier,
    item: TodoItem,
    onItemDetailClicked: (TodoItem) -> Unit,
    onRemove: (TodoItem) -> Unit,
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onItemDetailClicked(item) }
        .height(48.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(item.text, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.weight(1f))
        Text(DateTimeUtil.formatNoteDate(item.created), style = MaterialTheme.typography.body1)
        IconButton(onClick = { onRemove(item) }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Removes todo item",
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun TodoListContentPreview() {
    MyApplicationTheme {
        TodoListContent(
            AppState(
                todoList = listOf(
                    TodoItem("Test", DateTimeUtil.now())
                )
            )
        )
    }
}