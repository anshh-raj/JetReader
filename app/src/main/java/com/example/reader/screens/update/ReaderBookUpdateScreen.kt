package com.example.reader.screens.update

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.reader.components.InputField
import com.example.reader.components.RatingBar
import com.example.reader.components.ReaderAppBar
import com.example.reader.data.DataOrException
import com.example.reader.model.MBook
import com.example.reader.navigation.ReaderScreens
import com.example.reader.screens.home.HomeScreenViewModel

@Composable
fun BookUpdateScreen(
    navController: NavHostController,
    bookItemId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    BackHandler {
        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
    }
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Update Book",
                icon = Icons.AutoMirrored.Default.ArrowBack,
                showProfile = false,
                navController = navController,
                onBackArrowClicked = {
                    navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                }
            )
        }
    ) {
//        val bookInfo = produceState<DataOrException<List<MBook>,Boolean,Exception>>(
//            initialValue = DataOrException(
//                data = emptyList(),
//                loading = false,
//                e = Exception("")
//            )
//        ){
//            value = viewModel.data.value
//        }.value
        val bookInfo = viewModel.data.value

        if(bookInfo.loading == false){
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 3.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        shape = CircleShape,
                        shadowElevation = 4.dp,

                    ) {
                        ShowBookUpdate(bookInfo = bookInfo, bookItemId = bookItemId)
                    }

                    ShowSimpleForm(book = bookInfo.data!!.first { mBook ->
                            mBook.googleBookId == bookItemId
                        },
                        navController = navController
                    )
                }
            }
        }
        else{
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ShowSimpleForm(book: MBook, navController: NavHostController) {
    val noteText = remember {
        mutableStateOf("")
    }

    val isStartedReading = remember {
        mutableStateOf(true)
    }

    val isFinishedReading = remember {
        mutableStateOf(true)
    }

    val ratingVal = remember {
        mutableIntStateOf(0)
    }

    SimpleForm(
        defaultValue = if (book.notes.toString().isNotEmpty()) book.notes.toString()
         else ""
    ) { note ->
        noteText.value = note
    }

    Row (
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        TextButton(
            onClick = {
                isStartedReading.value = false
            },
            enabled = book.startedReading == null
        ) {
            if(book.startedReading == null){
                if (isStartedReading.value){
                    Text("Start Reading")
                }
                else{
                    Text(
                        "Started Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            }else{
                Text("Started on: ${book.startedReading}")
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        TextButton(
            onClick = {
                isFinishedReading.value = false
            },
            enabled = book.finishedReading == null
        ) {
            if(book.finishedReading == null){
                if (isFinishedReading.value){
                    Text("Mark as Read")
                }else{
                    Text(
                        "Finished Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            }else{
                Text("Finished on: ${book.finishedReading}")
            }

        }
    }
    Text(
        "Rating",
        modifier = Modifier.padding(3.dp)
    )
    book.rating!!.toInt().let { rating->
        RatingBar(
            rating = rating,
        ) {
            ratingVal.intValue = it
            Log.d("rating", "ShowSimpleForm: ${ratingVal.intValue}")
        }
    }

//    Spacer(modifier = Modifier.padding(bottom = 15.dp))
//
//    Row {  }
}

@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great Book!",
    onSearch: (String) -> Unit
){
    Column {
        val textFieldValue = rememberSaveable {
            mutableStateOf(defaultValue)
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(textFieldValue.value) {
            textFieldValue.value.trim().isNotEmpty()
        }

        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue,
            labelId = "Enter Your Thoughts",
            enabled = true,
            onAction = KeyboardActions{
                if(!valid) return@KeyboardActions
                onSearch(textFieldValue.value)
                keyboardController?.hide()
            }
        )
    }

}

@Composable
fun ShowBookUpdate(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
//        Spacer(modifier = Modifier.width(43.dp))
        if(bookInfo.data != null){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CardListItem(
                    book = bookInfo.data!!.first { mBook ->
                        mBook.googleBookId == bookItemId
                    },
                    onPressDetails = {}
                )

            }
        }
    }
}

@Composable
fun CardListItem(book: MBook, onPressDetails: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clickable { }
    ) {
        var url = ""
        try {
            url = book.photoUrl.toString()
        } catch (_: Exception) {
        }

        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .height(100.dp)
                .width(120.dp)
                .padding(4.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 120.dp,
                        topEnd = 20.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .align(Alignment.CenterVertically)
        )
        Column {
            Text(
                book.title.toString(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp),
//                    .width(150.dp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                book.authors.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 0.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                book.publishedDate.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}













