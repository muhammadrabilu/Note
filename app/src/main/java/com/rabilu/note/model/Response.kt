package com.rabilu.note.model

data class Response(
    var data: List<Note>? = null,
    var loading: Boolean = true,
    var errorMessage: String? = null
)
