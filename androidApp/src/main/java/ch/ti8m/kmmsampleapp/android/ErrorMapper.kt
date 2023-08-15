package ch.ti8m.kmmsampleapp.android

import ch.ti8m.kmmsampleapp.app.DomainError

fun DomainError.toReadableMessage() =
    when (this) {
        DomainError.NoInternet -> "Error"
    }
