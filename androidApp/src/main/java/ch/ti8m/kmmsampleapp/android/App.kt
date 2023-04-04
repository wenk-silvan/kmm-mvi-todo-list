package ch.ti8m.kmmsampleapp.android

import android.app.Application
import ch.ti8m.kmmsampleapp.app.AddItemStore
import ch.ti8m.kmmsampleapp.app.TodoListStore
import ch.ti8m.kmmsampleapp.core.repository.TodoListRepository
import ch.ti8m.kmmsampleapp.create
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private val appModule = module {
        single { TodoListRepository.create(context = get(), withLog = BuildConfig.DEBUG) }
        single { TodoListStore(get()) }
        single { AddItemStore(get(), get()) }
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule)
        }
    }
}