package ch.ti8m.kmmsampleapp.app

import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun createSettingsAppStore(
    settings: Settings
): AppStore {
    var observers: List<(AppState) -> Unit> = emptyList()
    var state = loadAppStateFromSettings(settings)
    return Store(
        state = { state },
        change = { change ->
            state = state.alter(change)
            observers.forEach { it(state) }
            saveAppStateToSettings(state, settings)
        },
        inject = { injectedState ->
            state = injectedState ?: AppState()
            observers.forEach { it(state) }
            saveAppStateToSettings(state, settings)
        },
        observe = { observers = observers + it }
    )
}

private fun saveAppStateToSettings(
    state: AppState,
    settings: Settings
) {
    try {
        val jsonString = Json.encodeToString(state)
        settings.putString(APP_STATE_KEY, jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private const val APP_STATE_KEY = "APP_STATE"

private fun loadAppStateFromSettings(settings: Settings): AppState {
    val state = settings.getStringOrNull(APP_STATE_KEY)
    return if (state == null) {
        Napier.d { "No app state available, creating new one" }
        val newState = AppState()
        saveAppStateToSettings(newState, settings)
        newState
    } else {
        Json.decodeFromString(state)
    }
}
