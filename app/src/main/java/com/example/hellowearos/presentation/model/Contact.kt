package com.example.hellowearos.presentation.model

data class Contact(
    val id: Long,
    val initials: String,
    val name: String,
    val avatarUrl: String?
) {

    fun toPrefrenceString(): String =
        listOf(id, initials, name, avatarUrl.orEmpty()).joinToString(",")

    companion object {

        fun String.toContact(): Contact {
            val (id, initials, name, avatarUrl) = split(",")
            return Contact(id.toLong(), initials, name, avatarUrl.ifBlank { null })
        }
    }
}