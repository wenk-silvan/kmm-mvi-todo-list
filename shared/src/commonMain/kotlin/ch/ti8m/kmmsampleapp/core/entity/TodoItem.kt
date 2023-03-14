package ch.ti8m.kmmsampleapp.core.entity

import kotlinx.datetime.LocalDateTime


data class TodoItem(
    val text: String,
    val created: LocalDateTime,
)
