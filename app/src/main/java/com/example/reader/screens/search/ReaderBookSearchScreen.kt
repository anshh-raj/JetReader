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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.reader.components.InputField
import com.example.reader.components.ReaderAppBar
import com.example.reader.model.Item
import com.example.reader.navigation.ReaderScreens

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModelNew: BookSearchViewModelNew = hiltViewModel()
) {
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
                        .padding(16.dp)
                ){query ->
                    viewModelNew.searchBooks(query)
                }

                Spacer(modifier = Modifier.height(13.dp))

                BookList(
                    navController = navController,
                    viewModelNew = viewModelNew
                )
            }
        }
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
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
    viewModelNew: BookSearchViewModelNew = hiltViewModel()
){

    val listOfBooks = viewModelNew.list

    if(viewModelNew.isLoading){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
    else{
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(listOfBooks){book->
                BookRow(book = book, navController = navController)
            }
        }
    }

//    val listOfBooks = listOf(
//        MBook(
//            id="bshsbd",
//            title = "hello again",
//            authors = "All of us",
//            notes = null
//        ),
//        MBook(
//            id="bshsbd",
//            title = "hello again",
//            authors = "All of us",
//            notes = null
//        ),
//        MBook(
//            id="bshsbd",
//            title = "hello again",
//            authors = "All of us",
//            notes = null
//        ),
//        MBook(
//            id="bshsbd",
//            title = "hello again",
//            authors = "All of us",
//            notes = null
//        ),
//        MBook(
//            id="bshsbd",
//            title = "hello again",
//            authors = "All of us",
//            notes = null
//        )
//
//    )


}

@Composable
fun BookRow(book: Item, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        onClick = {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        },
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
            var url = ""
            try {
                url = book.volumeInfo.imageLinks.thumbnail
            }catch (e: Exception){
                Log.d("ImageError", "BookRow: Book image not found")
            }
            AsyncImage(
                model = url,
                contentDescription = "",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )
            Column {
                Text(
                    book.volumeInfo.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    "Author: ${book.volumeInfo.authors}",
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "Date: ${book.volumeInfo.publishedDate}",
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "${book.volumeInfo.categories}",
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}





























