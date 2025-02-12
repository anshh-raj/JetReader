package com.example.reader.repository

import android.util.Log
import com.example.reader.data.DataOrException
import com.example.reader.model.MBook
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(private val queryBook: Query) {

    suspend fun getAllBooksFromDatabase(): DataOrException<List<MBook>, Boolean, Exception>{
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()
        try {
            dataOrException.loading = true
            dataOrException.data = queryBook.get().await().documents.map { documentSnapshot ->  
                documentSnapshot.toObject(MBook::class.java)!!
            }
            Log.d("FFFF", "getAllBooks: ${dataOrException.data}")
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false
            Log.d("FFFF", "getAllBooks: ${dataOrException.loading}")

        }catch (e: FirebaseFirestoreException){
            dataOrException.e = e
            Log.d("ERRORR", "getAllBooksFromDatabase: ${e.message}")
        }
        return dataOrException
    }

}