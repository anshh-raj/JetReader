package com.example.reader.screens.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.reader.components.FABContent
import com.example.reader.components.ListCard
import com.example.reader.components.ReaderAppBar
import com.example.reader.components.TitleSection
import com.example.reader.model.MBook
import com.example.reader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    val activity = LocalActivity.current
    BackHandler {
        activity?.finish()
    }
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "A.Reader",
                navController = navController
            )
        },
        floatingActionButton = {
            FABContent{
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            HomeContent(navController, viewModel)

        }
    }
}

@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel) {
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

//    if(viewModel.data.value.loading == false){
        val currentUser = FirebaseAuth.getInstance().currentUser
        var listOfBooks = emptyList<MBook>()

//        val listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
//            mBook.userId == currentUser?.uid.toString()
//        }
//
//        Log.d("Books", "HomeContent: $listOfBooks")


        if (!viewModel.data.value.data.isNullOrEmpty()) {
            listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
            }

            Log.d("Books", "HomeContent: $listOfBooks")
            Log.d("Books", "HomeContent: ${viewModel.data.value.loading}")
        }

        val currentUserName = if(!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
        }
        else "N/A"
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TitleSection(
                    label = "Your reading \n activity right now.."
                )

                Spacer(modifier = Modifier.fillMaxWidth(0.7f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .clickable {
                                navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                            }
                            .size(45.dp),
                        tint = Color.Gray
                    )
                    Text(
                        currentUserName.toString(),
                        modifier = Modifier
                            .padding(2.dp),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Red,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    HorizontalDivider()
                }
            }

            ReadingRightNowArea(
                listOfBooks = listOfBooks,
                navController = navController
            )

            TitleSection(
                label = "Reading List"
            )

            BookListArea(
                listOfBooks = listOfBooks,
                navController = navController
            )

        }
//    }
//    else{
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }

}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    val addedBook = listOfBooks.filter { mBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(addedBook){
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<MBook>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPressed: (String)-> Unit
) {
    val scrollState = rememberScrollState()

    if (viewModel.data.value.loading == true){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,

        ) {
            CircularProgressIndicator()
        }
    }else{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(280.dp)
                .horizontalScroll(scrollState)
        ) {
            if (listOfBooks.isEmpty()){
                Surface(
                    modifier = Modifier.padding(23.dp)
                ) {
                    Text(
                        "No Books Found. Add a Book",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                    )
                }
            }else{
                for (book in listOfBooks){
                    ListCard(
                        book = book
                    ){
                        onCardPressed(it)
                    }
                }
            }
        }
    }
}


@Composable
fun ReadingRightNowArea(
    listOfBooks: List<MBook>,
    navController: NavController
){
    val readingNow = listOfBooks.filter { mBook ->
        mBook.startedReading != null && mBook.finishedReading == null
    }
    HorizontalScrollableComponent(readingNow) {
        navController.navigate(ReaderScreens.UpdateScreen.name + "/$it")
    }

}

