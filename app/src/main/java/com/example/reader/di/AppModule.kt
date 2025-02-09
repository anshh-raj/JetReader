package com.example.reader.di

import com.example.reader.network.BooksAPI
import com.example.reader.repository.BookRepository
import com.example.reader.repository.BookRepositoryNew
import com.example.reader.repository.FireRepository
import com.example.reader.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFireRepository() = FireRepository(queryBook = FirebaseFirestore.getInstance().collection("books"))

    @Singleton
    @Provides
    fun provideBookRepositoryNew(api: BooksAPI) = BookRepositoryNew(api)

    @Singleton
    @Provides
    fun provideBookRepository(api: BooksAPI) = BookRepository(api)

    @Singleton
    @Provides
    fun provideBookApi(): BooksAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksAPI::class.java)
    }
}