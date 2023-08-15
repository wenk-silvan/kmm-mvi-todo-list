package ch.ti8m.kmmsampleapp.android.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ch.ti8m.kmmsampleapp.app.AppStore
import ch.ti8m.kmmsampleapp.app.MessageHandle
import ch.ti8m.kmmsampleapp.app.Screen
import org.koin.compose.koinInject

const val APP_TAG = "kmmSampleApp"
const val ROUTE_LIST = "route_list"
const val ROUTE_DETAIL = "route_detail"

@Composable
fun Navigation(
    navController: NavHostController,
    store: AppStore = koinInject(),
    messageHandle: MessageHandle
) {
    val state = store.getState()
    Navigator(navController, state.backStack)

    NavHost(navController = navController, startDestination = ROUTE_LIST) {
        composable(ROUTE_LIST) {
            TodoListScreen(store, messageHandle)
        }
        composable(ROUTE_DETAIL) {
            TodoItemScreen(store, messageHandle)
        }
    }
}

@Composable
private fun Navigator(navController: NavController, backStack: List<Screen>) {
    var currentBackStack by rememberSaveable { mutableStateOf(backStack) }
    if (backStack != currentBackStack) {
        val newScreens =
            backStack - currentBackStack.toSet() // Use better implementation to deal with screen duplication
        newScreens.forEach { screen ->
            val route = screen.toRoute()
            Log.d(APP_TAG, "Navigate to route: $route")
            navController.navigate(route)
        }
        val poppedScreens = currentBackStack - newScreens
        poppedScreens.forEach { screen ->
            Log.d(APP_TAG, "Popped screen: $screen")
            navController.popBackStack()
        }
        currentBackStack = backStack
    }
}

private fun Screen.toRoute() =
    when (this) {
        Screen.TODO_LIST -> ROUTE_LIST
        Screen.TODO_ITEM -> ROUTE_DETAIL
    }
