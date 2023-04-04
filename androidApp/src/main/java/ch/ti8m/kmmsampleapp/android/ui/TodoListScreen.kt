package ch.ti8m.kmmsampleapp.android.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ch.ti8m.kmmsampleapp.app.*
import ch.ti8m.kmmsampleapp.core.entity.TodoItem
import ch.ti8m.kmmsampleapp.core.util.DateTimeUtil
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoListScreen(store: TodoListStore, addItemStore: AddItemStore, scaffoldState: ScaffoldState) {
    val state by store.observeState().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your list",
            style = MaterialTheme.typography.h4,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AddItemRow(addItemStore, scaffoldState)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(
                items = state.todoList,
                key = { index, _ -> index },
            ) { _, todoItem ->
                TodoListItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth()
                        .height(48.dp),
                    item = todoItem,
                    onRemove = {
                        store.dispatch(TodoListAction.Remove(todoItem.created))
                    }
                )
            }
        }
    }
}

@Composable
private fun TodoListItem(
    modifier: Modifier = Modifier,
    item: TodoItem,
    onRemove: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(item.text)
        Spacer(modifier = Modifier.weight(1f))
        Text(DateTimeUtil.formatNoteDate(item.created))
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Removes todo item",
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun AddItemRow(store: AddItemStore, scaffoldState: ScaffoldState) {
    val state by store.observeState().collectAsState()
    val error = store.observeSideEffect()
        .filterIsInstance<AddItemSideEffect.Error>()
        .collectAsState(null)
    LaunchedEffect(error.value) {
        error.value?.exception?.message?.let {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.weight(1f),
            value = state.newItem,
            onValueChange = { store.dispatch(AddItemAction.UpdateNewItem(it)) },
            singleLine = true,
        )
        IconButton(onClick = {
            store.dispatch(AddItemAction.Add(text = state.newItem))
        }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adds todo item",
            )
        }
    }
}