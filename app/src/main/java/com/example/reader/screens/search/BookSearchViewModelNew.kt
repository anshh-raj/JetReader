package com.example.reader.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reader.data.Resource
import com.example.reader.model.Item
import com.example.reader.repository.BookRepositoryNew
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModelNew @Inject constructor(private val repositoryNew: BookRepositoryNew): ViewModel() {
    var list: List<Item> by mutableStateOf(listOf())
    var isLoading: Boolean by mutableStateOf(true)
    
    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) return@launch
            try {
                when(val response = repositoryNew.getBooks(query)){
                    is Resource.Error -> {
                        isLoading = true
                        Log.d("NETWORK", "searchBooks: Failed to get books ${response.message}")
                    }
                    is Resource.Success -> {
                        list = response.data!!
                        if(list.isNotEmpty()) isLoading = false
                    }
                    else -> {
                        isLoading = true
                    }
                }
            }catch (e:Exception){
                isLoading = true
                Log.d("NETWORK", "searchBooks: ${e.message}")
            }
        }
    }
}