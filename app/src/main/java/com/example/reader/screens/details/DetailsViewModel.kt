package com.example.reader.screens.details

import androidx.lifecycle.ViewModel
import com.example.reader.data.Resource
import com.example.reader.model.Item
import com.example.reader.repository.BookRepositoryNew
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repositoryNew: BookRepositoryNew): ViewModel() {
    suspend fun getBookInfo(bookId: String): Resource<Item>{
        return repositoryNew.getBookInfo(bookId)
    }
}