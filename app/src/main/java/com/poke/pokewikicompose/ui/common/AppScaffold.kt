package com.poke.pokewikicompose.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.poke.pokewikicompose.ui.AppSnackBar
import com.poke.pokewikicompose.ui.cover.CoverPage
import com.poke.pokewikicompose.ui.login.LoginPage
import com.poke.pokewikicompose.ui.searchMain.SearchMainPage
import com.poke.pokewikicompose.ui.register.RegisterPage
import com.poke.pokewikicompose.utils.COVER_PAGE
import com.poke.pokewikicompose.utils.LOGIN_PAGE
import com.poke.pokewikicompose.utils.SEARCH_MAIN_PAGE
import com.poke.pokewikicompose.utils.REGISTER_PAGE

@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState
            ) { data ->
                println("actionLabel = ${data.actionLabel}")
                AppSnackBar(data = data)
            }
        }
    ) {
        NavHost(
            modifier = Modifier
                .background(color = Color(0xFFEFEFEF))
                .fillMaxSize(),
            navController = navController,
            startDestination = COVER_PAGE
        ) {
            composable(route = COVER_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    Color.Black, darkIcons = MaterialTheme.colors.isLight
                )
                CoverPage(navController = navController)
            }
            composable(route = LOGIN_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    Color(0xFFEFEFEF), darkIcons = MaterialTheme.colors.isLight
                )
                LoginPage(navCtrl = navController, scaffoldState = scaffoldState)
            }
            composable(route = REGISTER_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    Color(0xFFEFEFEF), darkIcons = MaterialTheme.colors.isLight
                )
                RegisterPage(navCtrl = navController, scaffoldState = scaffoldState)
            }
            composable(
                route = SEARCH_MAIN_PAGE,
            ) {
                rememberSystemUiController().setNavigationBarColor(
                    Color(0xFFEFEFEF), darkIcons = MaterialTheme.colors.isLight
                )
                SearchMainPage(navCtrl = navController, scaffoldState = scaffoldState)
            }
        }
    }
}