package com.example.reader.repository

import com.example.reader.data.Resource
import com.example.reader.model.Item
import com.example.reader.network.BooksAPI
import javax.inject.Inject

class BookRepositoryNew @Inject constructor(private val api: BooksAPI) {
    suspend fun getBooks(searchQuery: String): Resource<List<Item>>{
        return try {
            Resource.Loading(true)
            val itemList = api.getAllBooks(searchQuery).items
            if(itemList.isNotEmpty()) Resource.Loading(false)
            Resource.Success(itemList)
        }catch (e: Exception){
            Resource.Error(message = e.message.toString())
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item>{
        return try {
            Resource.Loading(true)
            val item = api.getBookInfo(bookId)
            if (item.toString().isNotEmpty()) Resource.Loading(false)
            Resource.Success(item)
        }catch (e: Exception){
            Resource.Error(message = e.message.toString())
        }
    }
}