package com.example.reader.model

data class MBook(
    var id: String? = null,
    var title: String? = null,
    var authors: String? = null,
    var notes: String? = null
)

//keeping this var as this will make it easy to work with firestore
