package ch.ti8m.kmmsampleapp

import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun TodoListRepository.Companion.create(withLog: Boolean) = TodoListRepository(
    // Inject platform specific implementations
).also {
    if (withLog) Napier.base(DebugAntilog())
}