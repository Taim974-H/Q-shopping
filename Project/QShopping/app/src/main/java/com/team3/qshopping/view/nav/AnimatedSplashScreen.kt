package com.team3.qshopping.view.nav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team3.qshopping.R
import com.team3.qshopping.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(
    onLoggedIn: () -> Unit,
    onNotLoggedIn: () -> Unit
) {
    val splashViewModel = SplashViewModel()

    Splash(
        onLoggedIn = onLoggedIn,
        onNotLoggedIn = onNotLoggedIn,
        splashViewModel = splashViewModel
    )
}

@Composable
private fun Splash(
    onLoggedIn: () -> Unit,
    onNotLoggedIn: () -> Unit,
    splashViewModel: SplashViewModel
) {
    if (splashViewModel.loggedIn) {
        onLoggedIn()
    }

    if (splashViewModel.notLoggedIn) {
        onNotLoggedIn()
    }

    var startAnimation by remember { mutableStateOf(false) }
    val loadTime = 500
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = loadTime)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = !startAnimation
        delay(loadTime + 500L)
        splashViewModel.checkAuth()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            modifier = Modifier.size(120.dp),
            alpha = alphaAnim.value,
            contentDescription = "QShopping"
        )
    }
}
