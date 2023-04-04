package ch.ti8m.kmmsampleapp.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import ch.ti8m.kmmsampleapp.android.ui.TodoListScreen
import ch.ti8m.kmmsampleapp.app.AddItemSideEffect
import ch.ti8m.kmmsampleapp.app.AddItemStore
import ch.ti8m.kmmsampleapp.app.TodoListStore
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val store: TodoListStore by inject()
                val addItemStore: AddItemStore by inject()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = { hostState ->
                        SnackbarHost(
                            hostState = hostState,
                            modifier = Modifier.padding(
                                WindowInsets.systemBars.asPaddingValues()
                            )
                        )
                    }
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(it),
                        color = MaterialTheme.colors.background
                    ) {
                        TodoListScreen(store, addItemStore, scaffoldState)
                    }
                }
            }
        }
    }
}
