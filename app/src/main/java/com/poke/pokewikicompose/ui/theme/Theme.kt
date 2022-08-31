package com.poke.pokewikicompose.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = AppColors(
    themeUi = PokeBallRed,
    mainColor = PokeBallRed,
    background = BackGround,
    card = Color.White,
    icon = RoyalBlue,
    info = Info,
    warn = Warn,
    success = Success,
    error = Error,
    primaryBtnBg = PokeBallRed,
    secondBtnBg = RegisterBlue,
)

private val DarkColorPalette = AppColors(
    themeUi = PokeBallRed,
    mainColor = PokeBallRed,
    background = BackGround,
    card = Color.White,
    icon = RoyalBlue,
    info = Info,
    warn = Warn,
    success = Success,
    error = Error,
    primaryBtnBg = PokeBallRed,
    secondBtnBg = RegisterBlue,
)

var LocalAppColors = compositionLocalOf {
    LightColorPalette
}

@Stable
object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    enum class Theme {
        Light, Dark
    }
}

@Stable
class AppColors(
    themeUi: Color,
    background: Color,
    mainColor: Color,
    card: Color,
    icon: Color,
    info: Color,
    warn: Color,
    success: Color,
    error: Color,
    primaryBtnBg: Color,
    secondBtnBg: Color,
) {
    var themeUi: Color by mutableStateOf(themeUi)
        internal set
    var background: Color by mutableStateOf(background)
        private set
    var mainColor: Color by mutableStateOf(mainColor)
        internal set
    var card: Color by mutableStateOf(card)
        private set
    var icon: Color by mutableStateOf(icon)
        private set
    var info: Color by mutableStateOf(info)
        private set
    var warn: Color by mutableStateOf(warn)
        private set
    var success: Color by mutableStateOf(success)
        private set
    var error: Color by mutableStateOf(error)
        private set
    var primaryBtnBg: Color by mutableStateOf(primaryBtnBg)
        internal set
    var secondBtnBg: Color by mutableStateOf(secondBtnBg)
        private set
}

@Composable
fun AppTheme(
    theme: AppTheme.Theme = AppTheme.Theme.Light,
    content: @Composable () -> Unit
) {

    val targetColors = when (theme) {
        AppTheme.Theme.Light -> {
            LightColorPalette.themeUi = PokeBallRed
            LightColorPalette.primaryBtnBg = PokeBallRed
            LightColorPalette
        }
        AppTheme.Theme.Dark -> DarkColorPalette
    }

    val themeUi = animateColorAsState(targetColors.themeUi, TweenSpec(600))
    val background = animateColorAsState(targetColors.background, TweenSpec(600))
    val mainColor = animateColorAsState(targetColors.mainColor, TweenSpec(600))
    val card = animateColorAsState(targetColors.card, TweenSpec(600))
    val icon = animateColorAsState(targetColors.icon, TweenSpec(600))
    val info = animateColorAsState(targetColors.info, TweenSpec(600))
    val warn = animateColorAsState(targetColors.warn, TweenSpec(600))
    val success = animateColorAsState(targetColors.success, TweenSpec(600))
    val error = animateColorAsState(targetColors.error, TweenSpec(600))
    val primaryBtnBg = animateColorAsState(targetColors.primaryBtnBg, TweenSpec(600))
    val secondBtnBg = animateColorAsState(targetColors.secondBtnBg, TweenSpec(600))
    val appColors = AppColors(
        themeUi = themeUi.value,
        background = background.value,
        mainColor = mainColor.value,
        card = card.value,
        icon = icon.value,
        primaryBtnBg = primaryBtnBg.value,
        secondBtnBg = secondBtnBg.value,
        info = info.value,
        warn = warn.value,
        success = success.value,
        error = error.value,
    )

    val systemUiCtrl = rememberSystemUiController()
    systemUiCtrl.setStatusBarColor(appColors.themeUi)
    systemUiCtrl.setNavigationBarColor(appColors.themeUi)
    systemUiCtrl.setSystemBarsColor(appColors.themeUi)

    CompositionLocalProvider(LocalAppColors provides appColors, content = content)
}