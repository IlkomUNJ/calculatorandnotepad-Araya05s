package com.example.basicscodelab

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to the Home Screen!",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "with calculator and Notepad(WIP)",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    // Navigate to the 'detail_screen' route
                    navController.navigate("calculator_screen")
                },
                contentPadding = PaddingValues(16.dp)
            ) {
                Text("Go to Calculator Screen")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    navController.navigate("notepad_screen")
                },
                contentPadding = PaddingValues(16.dp)
            ) {
                Text("Go to Notepad Screen")
            }
        }
}
