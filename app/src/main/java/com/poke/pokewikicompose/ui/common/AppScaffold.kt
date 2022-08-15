package com.poke.pokewikicompose.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.orhanobut.logger.Logger
import com.poke.pokewikicompose.ui.AppSnackBar
import com.poke.pokewikicompose.ui.about.AboutPage
import com.poke.pokewikicompose.ui.cover.CoverPage
import com.poke.pokewikicompose.ui.login.LoginPage
import com.poke.pokewikicompose.ui.main.MainPage
import com.poke.pokewikicompose.ui.profile.ProfilePage
import com.poke.pokewikicompose.ui.register.RegisterPage
import com.poke.pokewikicompose.ui.searchMain.SearchMainPage
import com.poke.pokewikicompose.utils.*

@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val mainPageList = listOf<@Composable () -> Unit>(
        { AboutPage(scaffoldState, navController)},
        { SearchMainPage(navController, scaffoldState) },
        { ProfilePage(scaffoldState, navController)}
    )

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = scaffoldState.snackbarHostState
            ) { data ->
                Logger.i("actionLabel = ${data.actionLabel}")
                AppSnackBar(data = data)
            }
        }
    ) { padding ->
        NavHost(
            modifier = Modifier
                .background(color = Color(0xFFEFEFEF))
                .fillMaxSize()
                .padding(padding),
            navController = navController,
            startDestination = COVER_PAGE
        ) {
            composable(route = COVER_PAGE) {
                rememberSystemUiController().setNavigationBarColor(
                    Color.Black, darkIcons = MaterialTheme.colors.isLight
                )
                CoverPage(navController = navController)
            }
            composable(route = "$COVER_PAGE?skipType={skipType}", arguments = listOf(
                navArgument("skipType"){
                    defaultValue = "Cover"
                    type = NavType.StringType
                }
            )) {
                rememberSystemUiController().setNavigationBarColor(
                    Color.Black, darkIcons = MaterialTheme.colors.isLight
                )
                val argument = requireNotNull(it.arguments)
                val type = argument.getString("skipType")
                CoverPage(navController = navController, skipType = type?:"Cover")
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
                route = MAIN_PAGE,
            ) {
                rememberSystemUiController().setNavigationBarColor(
                    Color(0xFFEFEFEF), darkIcons = MaterialTheme.colors.isLight
                )
                MainPage(
                    navCtrl = navController,
                    scaffoldState = scaffoldState,
                    mainPageList = mainPageList
                )
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