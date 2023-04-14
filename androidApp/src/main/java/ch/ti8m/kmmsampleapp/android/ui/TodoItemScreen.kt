package ch.ti8m.kmmsampleapp.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ch.ti8m.kmmsampleapp.app.TodoListAction
import ch.ti8m.kmmsampleapp.app.TodoListStore
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil

@Composable
fun TodoItemScreen(store: TodoListStore) {
    val state by store.observeState().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Item Details", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = DateTimeUtil.formatNoteDate(state.editItem.created),
            style = MaterialTheme.typography.body1,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier.weight(1f),
                value = state.editItem.text,
                onValueChange = { store.dispatch(TodoListAction.UpdateEditItemText(it)) },
                singleLine = true,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { store.dispatch(TodoListAction.SaveEditItem) }) {
            Text(text = "Save")
        }
    }
}