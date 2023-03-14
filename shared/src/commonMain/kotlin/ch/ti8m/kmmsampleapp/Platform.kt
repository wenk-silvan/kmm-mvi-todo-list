package ch.ti8m.kmmsampleapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform