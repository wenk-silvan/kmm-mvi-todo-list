package ch.ti8m.kmmsampleapp

import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import com.russhwolf.settings.NSUserDefaultsSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.Foundation.NSUserDefaults

fun TodoListRepository.Companion.create(
    withLog: Boolean,
) = TodoListRepository(
    NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults()),
).also {
    if (withLog) Napier.base(DebugAntilog())
}