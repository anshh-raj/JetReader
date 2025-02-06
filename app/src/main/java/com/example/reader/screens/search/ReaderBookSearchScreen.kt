package com.example.reader.screens.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.reader.components.InputField
import com.example.reader.components.ReaderAppBar
import com.example.reader.model.MBook

@Composable
fun SearchScreen(navController: NavHostController, viewModel: BookSearchViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                navController = navController,
                showProfile = false
            ){
                navController.popBackStack()
            }
        }
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    viewModel = viewModel
                ){query ->
                    viewModel.searchBooks(query)

                }

                Spacer(modifier = Modifier.height(13.dp))

                BookList(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    viewModel: BookSearchViewModel,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String)-> Unit = {}
){
    Column(
        modifier = modifier
    ) {
        val searchQueryState = rememberSaveable{
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(
            valueState = searchQueryState,
            labelId = hint,
            enabled = true,
            imeAction = ImeAction.Search,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            }
        )
    }
}

@Composable
fun BookList(
    navController: NavHostController,
    viewModel: BookSearchViewModel,
){

    if(viewModel.listOfBooks.value.loading == true){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
    else{
        Log.d("BOOKDATA", "BookList: ${viewModel.listOfBooks.value.data}")
    }

    val listOfBooks = listOf(
        MBook(
            id="bshsbd",
            title = "hello again",
            authors = "All of us",
            notes = null
        ),
        MBook(
            id="bshsbd",
            title = "hello again",
            authors = "All of us",
            notes = null
        ),
        MBook(
            id="bshsbd",
            title = "hello again",
            authors = "All of us",
            notes = null
        ),
        MBook(
            id="bshsbd",
            title = "hello again",
            authors = "All of us",
            notes = null
        ),
        MBook(
            id="bshsbd",
            title = "hello again",
            authors = "All of us",
            notes = null
        )

    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(listOfBooks){book->
            BookRow(book = book, navController = navController)
        }
    }
}

@Composable
fun BookRow(book: MBook, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        onClick = {},
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = "http://books.google.com/books/content?id=qKFDDAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                contentDescription = "",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )
            Column {
                Text(
                    book.title.toString(),
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    "Author: ${book.authors.toString()}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}





























