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
import ch.ti8m.kmmsampleapp.app.TodoListSideEffect
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
                val scaffoldState = rememberScaffoldState()
                val error = store.observeSideEffect()
                    .filterIsInstance<TodoListSideEffect.Error>()
                    .collectAsState(null)
                LaunchedEffect(error.value) {
                    error.value?.let {
                        scaffoldState.snackbarHostState.showSnackbar(
                            it.error.message.toString()
                        )
                    }
                }
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colors.background
                    ) {
                        TodoListScreen(store)
                    }
                }
            }
        }
    }
}
