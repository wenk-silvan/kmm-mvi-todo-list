package ch.ti8m.kmmsampleapp.core.entity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val text: String,
    val created: LocalDateTime,
)
