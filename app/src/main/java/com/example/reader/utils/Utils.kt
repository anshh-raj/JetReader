package com.example.reader.utils

import android.icu.text.DateFormat
import com.google.firebase.Timestamp

fun formatDate(timestamp: Timestamp?): String{
    if (timestamp == null){
        return "N/A"
    }
    val date = DateFormat.getDateInstance()
        .format(timestamp.toDate())
        .toString()
        .split(",")[0] // March 20, 2025
    return date
}