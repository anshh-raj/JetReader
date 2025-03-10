package com.example.reader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reader.screens.details.BookDetailsScreen
import com.example.reader.screens.home.Home
import com.example.reader.screens.home.HomeScreenViewModel
import com.example.reader.screens.login.ReaderLoginScreen
import com.example.reader.screens.search.BookSearchViewModel
import com.example.reader.screens.search.BookSearchViewModelNew
import com.example.reader.screens.search.SearchScreen
import com.example.reader.screens.splashScreen.ReaderSplashScreen
import com.example.reader.screens.stats.ReaderStatsScreen
import com.example.reader.screens.update.BookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name){
        composable(ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name){
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            Home(navController = navController, viewModel)
        }

        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}",
            arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){ backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }
        composable(ReaderScreens.SearchScreen.name){
            val viewModel = hiltViewModel<BookSearchViewModel>()
            val viewModelNew = hiltViewModel<BookSearchViewModelNew>()
            SearchScreen(navController = navController, viewModelNew)
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId"){
                type = NavType.StringType
            })
        ){ backStackEntry ->
            backStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navController, bookItemId = it.toString())
            }
        }
        composable(ReaderScreens.ReaderStatsScreen.name){
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController = navController, viewModel)
        }
        composable(ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }
    }
}