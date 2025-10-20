package com.example.basicscodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.basicscodelab.ui.theme.BasicsCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var modifier : Modifier
        enableEdgeToEdge()
        setContent {
            BasicsCodelabTheme {
                modifier = Modifier.fillMaxSize()
                AppNavigation()
//                TopWidget.PrimaryWidget(modifier = Modifier.fillMaxSize())
            }
        }

    }
}

@Composable
fun AppNavigation() {
    // 1. Create and remember the NavController
    val navController = rememberNavController()

    // 2. Define the NavHost and the navigation graph
    NavHost(
        navController = navController,
        startDestination = "home_screen" // Define the initial screen
    ) {
        // Define the 'home_screen' destination
        composable("home_screen") {
            HomeScreen(navController = navController)
        }

        // Define the 'detail_screen' destination
        composable("calculator_screen") {
            CalculatorScreen_Screen.setupLayout(navController = navController)
        }

        composable("notepad_screen") {
            NotepadScreenUI.setupLayout(navController = navController)
        }
    }
}


//
//val Navy = Color(0xFF073042)
//val Blue = Color(0xFF4285F4)
//val LightBlue = Color(0xFFD7EFFE)
//val Chartreuse = Color(0xFFEFF7CF)
//
//
//private val DarkColorScheme = darkColorScheme(
//    surface = Blue,
//    onSurface = Navy,
//    primary = Navy,
//    onPrimary = Chartreuse
//)
//
//private val LightColorScheme = lightColorScheme(
//    surface = Blue,
//    onSurface = Color.White,
//    primary = LightBlue,
//    onPrimary = Navy
//)
//
//
//@Composable
//fun OnboardingScreen(modifier: Modifier = Modifier) {
//    // TODO: This state should be hoisted
//    var shouldShowOnboarding = rememberSaveable { mutableStateOf(true) }
//
//    Column(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Welcome to the Basics Codelab!")
//        Button(
//            modifier = Modifier.padding(vertical = 24.dp),
//            onClick = { shouldShowOnboarding.value = false }
//        ) {
//            Text("Continue")
//        }
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BasicsCodelabTheme {
//         CalculatorScreenUI.setupLayout()
////        OnboardingScreen()
////        TopWidget.Greeting(name = "Android")
//    }
//}


