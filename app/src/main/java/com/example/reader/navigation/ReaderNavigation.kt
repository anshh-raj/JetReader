package com.example.reader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reader.screens.details.BookDetailsScreen
import com.example.reader.screens.home.Home
import com.example.reader.screens.login.ReaderLoginScreen
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
            Home(navController = navController)
        }
        composable(ReaderScreens.DetailScreen.name){
            BookDetailsScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }
        composable(ReaderScreens.UpdateScreen.name){
            BookUpdateScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderStatsScreen.name){
            ReaderStatsScreen(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }
    }
}