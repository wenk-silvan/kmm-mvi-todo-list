package ch.ti8m.kmmsampleapp

import android.content.Context
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun TodoListRepository.Companion.create(
    context: Context,
    withLog: Boolean,
) = TodoListRepository(
    settings = SharedPreferencesSettings(
        context.getSharedPreferences("todo_list_app", Context.MODE_PRIVATE)
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}