package ch.ti8m.kmmsampleapp.android

import android.app.Application
import ch.ti8m.kmmsampleapp.initializeApp
import ch.ti8m.kmmsampleapp.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initializeApp()
    }

    private val appModule = module {}

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)

            androidContext(this@App)
            modules(appModule, repositoryModule)
        }
    }
}