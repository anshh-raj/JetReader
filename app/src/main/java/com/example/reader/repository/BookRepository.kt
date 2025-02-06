package com.example.reader.repository

import com.example.reader.data.DataOrException
import com.example.reader.model.Item
import com.example.reader.network.BooksAPI
import javax.inject.Inject

class BookRepository @Inject constructor(private val api: BooksAPI) {
    private val dataOrException = DataOrException<List<Item>, Boolean, Exception>()
    private val bookInfoDataOrException = DataOrException<Item, Boolean, Exception>()

    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception>{
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuery).items
            if(dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
        }catch (e: Exception){
            dataOrException.e = e
        }
        return dataOrException
    }

    suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception>{
        try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookId)
            if(bookInfoDataOrException.data.toString().isNotEmpty()) bookInfoDataOrException.loading = false
        }catch (e: Exception){
            bookInfoDataOrException.e = e
        }
        return bookInfoDataOrException
    }
}