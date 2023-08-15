package ch.ti8m.kmmsampleapp.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.ti8m.kmmsampleapp.app.AppStore
import ch.ti8m.kmmsampleapp.app.MessageHandle
import ch.ti8m.kmmsampleapp.app.todo.TodoItemMessage
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil

@Composable
fun TodoItemScreen(store: AppStore, messageHandle: MessageHandle) {
    TodoItemContent(
        store.getState().todoItem!!,
        onItemTextChanged = { messageHandle(TodoItemMessage.UpdateItemText(it)) },
        onSaveClicked = { messageHandle(TodoItemMessage.SaveItem()) }
    )
}

@Composable
fun TodoItemContent(
    todoItem: TodoItem,
    onItemTextChanged: (String) -> Unit = {},
    onSaveClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Item Details", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = DateTimeUtil.formatNoteDate(todoItem.created),
            style = MaterialTheme.typography.body1,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.weight(1f),
                value = todoItem.text,
                onValueChange = onItemTextChanged,
                singleLine = true,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onSaveClicked() }) {
            Text(text = "Save")
        }
    }
}

@Preview
@Composable
private fun TodoItemContentPreview() {
    MyApplicationTheme {
        TodoItemContent(
            TodoItem("Test", DateTimeUtil.now())
        )
    }
}
