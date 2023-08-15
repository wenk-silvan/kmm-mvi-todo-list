package ch.ti8m.kmmsampleapp.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ch.ti8m.kmmsampleapp.android.ui.MyApplicationTheme
import ch.ti8m.kmmsampleapp.android.ui.Navigation
import ch.ti8m.kmmsampleapp.app.AppState
import ch.ti8m.kmmsampleapp.app.AppStore
import ch.ti8m.kmmsampleapp.app.MessageHandle
import ch.ti8m.kmmsampleapp.app.createAppDomain
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val store: AppStore by inject()
    private val repository: TodoListRepository by inject()
    private val messageHandle: MessageHandle = createAppDomain(store, repository)

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                var state = store.state()
                store.observe {
                    state = store.state()
                }
                val error = state.error
                LaunchedEffect(error) {
                    error?.let {
                        scaffoldState.snackbarHostState.showSnackbar(it.toReadableMessage())
                    }
                }
                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = { hostState ->
                        SnackbarHost(
                            hostState = hostState,
                            modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues())
                        )
                    }
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(it),
                        color = MaterialTheme.colors.background
                    ) {
                        Navigation(navController, store, messageHandle)
                    }
                }
            }
        }
    }
}
