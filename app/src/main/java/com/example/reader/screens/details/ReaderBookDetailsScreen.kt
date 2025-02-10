package com.example.reader.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.reader.components.ReaderAppBar
import com.example.reader.components.RoundedButton
import com.example.reader.data.Resource
import com.example.reader.model.Item
import com.example.reader.model.MBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Details",
                icon = Icons.AutoMirrored.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ){
                navController.popBackStack()
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val bookInfo = produceState<Resource<Item>>(
                    initialValue = Resource.Loading()
                ) {
                    value = viewModel.getBookInfo(bookId)
                }.value

                if(bookInfo.data == null){
                    LinearProgressIndicator()
                }
                else{
                    ShowBookDetails(bookInfo, navController)
                }
            }

        }
    }
}

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavHostController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(34.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            var url = ""
            try {
                if (bookData != null) {
                    url = bookData.imageLinks.smallThumbnail
                }
            }catch (e: Exception){
                Log.d("ImageError", "BookRow: Book image not found")
            }
            AsyncImage(
                model = url,
                contentDescription = "Book Image",
                modifier = Modifier
                    .width(90.dp)
                    .height(90.dp)
                    .padding(1.dp),
                contentScale = ContentScale.Fit
            )
        }
        Text(
            bookData?.title.toString(),
            style = MaterialTheme.typography.headlineLarge,
            maxLines = 19,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "Authors: ${bookData?.authors.toString()}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Page Count: ${bookData?.pageCount.toString()}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Categories: ${bookData?.categories.toString()}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Published: ${bookData?.publishedDate.toString()}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(5.dp))
        val cleanDescription = HtmlCompat.fromHtml(bookData?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

        val localDims = LocalContext.current.resources.displayMetrics
        Surface(
            modifier = Modifier
                .height(localDims.heightPixels.dp.times(0.15f))
                .padding(4.dp),
            shape = RectangleShape,
            border = BorderStroke(1.dp, Color.DarkGray)
        ) {
            LazyColumn (
                modifier = Modifier.padding(3.dp)
            ){
                item{
                    Text(
                        cleanDescription,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            var title = ""
            try {
                if (bookData != null) {
                    title = bookData.title
                }
            }catch (_:Exception){}

            var authors = ""
            try {
                if (bookData != null) {
                    authors = bookData.authors.toString()
                }
            }catch (_:Exception){}

            var description = ""
            try {
                if (bookData != null) {
                    description = bookData.description
                }
            }catch (_:Exception){}

            var categories = ""
            try {
                if (bookData != null) {
                    categories = bookData.categories.toString()
                }
            }catch (_:Exception){}

            var photoUrl = ""
            try {
                if (bookData != null) {
                    photoUrl = bookData.imageLinks.smallThumbnail
                }
            }catch (_:Exception){}

            var publishedDate = ""
            try {
                if (bookData != null) {
                    publishedDate = bookData.publishedDate
                }
            }catch (_:Exception){}

            var pageCount = ""
            try {
                if (bookData != null) {
                    pageCount = bookData.pageCount.toString()
                }
            }catch (_:Exception){}

            RoundedButton(
                label = "Save"
            ){
                // save this book to firestore database
                val book = MBook(
                    title = title,
                    authors = authors,
                    description = description,
                    categories = categories,
                    notes = "",
                    photoUrl = photoUrl,
                    publishedDate = publishedDate,
                    pageCount = pageCount,
                    rating = 0.0,
                    googleBookId = googleBookId,
                    userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),

                )
                try{
                    saveToFirebase(book, navController)
                }catch (e:Exception){
                    Log.d("FError", "ShowBookDetails: ${e.message}")
                }
            }
            Spacer(modifier = Modifier.width(25.dp))
            RoundedButton(
                label = "Cancel"
            ){
                navController.popBackStack()
            }
        }
    }
}

fun saveToFirebase(book: MBook, navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if(book.toString().isNotEmpty()){
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful)
                            navController.popBackStack()
                    }
                    .addOnFailureListener {
                        Log.d("Errorr", "saveToFirebase: Error updating doc", it)
                    }
            }
    }else{}
}
