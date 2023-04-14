package ch.ti8m.kmmsampleapp.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ch.ti8m.kmmsampleapp.app.TodoListSideEffect
import ch.ti8m.kmmsampleapp.app.TodoListStore
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val ROUTE_LIST = "route_list"
const val ROUTE_DETAIL = "route_detail"

@Composable
fun Navigation(navController: NavHostController, store: TodoListStore) {
    store.NavigationListener<TodoListSideEffect.NavigateToItemDetailScreen> {
        navController.navigate(ROUTE_DETAIL)
    }
    store.NavigationListener<TodoListSideEffect.NavigateToItemListScreen> {
        navController.popBackStack()
    }

    NavHost(navController = navController, startDestination = ROUTE_LIST) {
        composable(ROUTE_LIST) {
            TodoListScreen(store)
        }
        composable(ROUTE_DETAIL) {
            TodoItemScreen(store)
        }
    }
}

@Composable
private inline fun <reified T> TodoListStore.NavigationListener(crossinline execute: () -> Unit) {
    val scope = rememberCoroutineScope()
    this.observeSideEffect()
        .filterIsInstance<T>()
        .onEach { execute() }
        .launchIn(scope)
}