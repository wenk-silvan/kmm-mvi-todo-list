package ch.ti8m.kmmsampleapp.app

import io.github.aakira.napier.Napier


internal fun createLoggingFeature(store: AppStore) = { message: Message ->
    Napier.d { ">>>> Message: $message" }
    Napier.d { "Current state: ${store.state()}" }
}
