package com.example.reader.screens.stats

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.reader.components.ReaderAppBar
import com.example.reader.model.MBook
import com.example.reader.navigation.ReaderScreens
import com.example.reader.screens.home.HomeScreenViewModel
import com.example.reader.utils.formatDate
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Composable
fun ReaderStatsScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    BackHandler {
        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
    }
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser


    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Stats",
                navController = navController,
                icon = Icons.AutoMirrored.Default.ArrowBack,
                showProfile = false
            ){
                navController.navigate(ReaderScreens.ReaderHomeScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            books = if(!viewModel.data.value.data.isNullOrEmpty()){
                viewModel.data.value.data!!.filter { mBook ->
                    mBook.userId == currentUser?.uid
                }
            }else{
                emptyList()
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(45.dp)
//                            .padding(2.dp)
//                    ){
//
//                    }
                    Icon(
                        imageVector = Icons.Sharp.Person,
                        contentDescription = "icon",
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 20.dp, end = 10.dp)
                    )
                    Text(
                        "Hi, ${currentUser?.email.toString().split("@")[0].uppercase(Locale.ROOT)}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    val readBooksList = if (!viewModel.data.value.data.isNullOrEmpty()){
                        books.filter { mBook ->
                            (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                        }
                    }else emptyList()

                    val readingBooks = if(viewModel.data.value.loading == false)
                        books.filter { mBook ->
                            (mBook.startedReading != null) && (mBook.finishedReading == null)
                    }else{
                        emptyList()
                    }

                    Column(
                        modifier = Modifier.padding(start = 25.dp, top = 4.dp, bottom = 4.dp, end = 25.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            "Your Stats",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        HorizontalDivider()
                        Text(
                            "You're reading: ${readingBooks.size} books",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "You've read: ${readBooksList.size} books",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                if(viewModel.data.value.loading == true){
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }else{
                    HorizontalDivider()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        val readBooksList = if (viewModel.data.value.loading == false){
                            books.filter { mBook ->
                                (mBook.userId == currentUser?.uid) && (mBook.finishedReading != null)
                            }
                        }else emptyList()
                        items(readBooksList){mBook ->
                            StatsBookRow(
                                mBook,
                                navController
                            )

                        }
                    }
                }

            }
        }

    }

}

@Composable
fun StatsBookRow(book: MBook, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        onClick = {
            navController.navigate(ReaderScreens.UpdateScreen.name + "/${book.googleBookId}")
        },
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
                url = book.photoUrl.toString()
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        book.title.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
//                        modifier = Modifier.weight(0.9f)
                    )

                    if(book.rating!! >= 4){
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "icon",
                            tint = Color.Green.copy(alpha = 0.5f)
//                        modifier = Modifier.weight(0.1f)
                        )
                    }
                }
                Text(
                    "Author: ${book.authors}",
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "Started: ${formatDate(book.startedReading)}",
                    softWrap = true,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    "Finished: ${formatDate(book.finishedReading)}",
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}