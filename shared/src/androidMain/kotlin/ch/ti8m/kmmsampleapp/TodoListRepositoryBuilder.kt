package ch.ti8m.kmmsampleapp

import android.content.Context
import ch.ti8m.kmmsampleapp.core.datasource.storage.TodoListPreferences
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun TodoListRepository.Companion.create(
    context: Context,
    withLog: Boolean,
) = TodoListRepository(
    preferences = TodoListPreferences(context)
).also {
    if (withLog) Napier.base(DebugAntilog())
}