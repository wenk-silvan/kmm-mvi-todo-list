package ch.ti8m.kmmsampleapp

import android.content.Context
import ch.ti8m.kmmsampleapp.app.createSettingsAppStore
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    single<Settings> {
        SharedPreferencesSettings(
            androidContext().getSharedPreferences("todo_list_app", Context.MODE_PRIVATE)
        )
    }
    singleOf(::createSettingsAppStore)
    singleOf(::TodoListRepository)
}

fun initializeApp() {
    if (BuildConfig.DEBUG) {
        Napier.base(DebugAntilog())
    }
}
