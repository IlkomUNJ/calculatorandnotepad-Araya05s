package com.example.basicscodelab

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object TopWidget {
    @Composable
    fun PrimaryWidget(modifier: Modifier = Modifier) {

        var shouldShowOnboarding = rememberSaveable() { mutableStateOf(true) }

        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            if (shouldShowOnboarding.value) {
                OnboardingScreen(onContinueClicked = { shouldShowOnboarding.value = false })
            } else {
                CreateManyGreeting(Modifier)
            }
        }
    }

    @Composable

    fun OnboardingScreen(
        onContinueClicked: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Continue")
            }
        }

    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        var expanded = rememberSaveable { mutableStateOf(false) }

        val extraPadding = animateDpAsState(
            if (expanded.value) 48.dp else 0.dp,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow
            )
        )

        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Column(
                    modifier.weight(1f)
                    .padding(bottom = extraPadding.value)
                ) {
                    Text(
                        text = "WAH ADA $name!!"
                    )
                }
                ElevatedButton(
                    onClick = { expanded.value = !expanded.value }
                ) {
                    Text(if (expanded.value) "Pake Nanya\uD83D\uDE02" else "Serius!?")               }
            }
        }
    }

    @Composable
    fun CreateManyGreeting (
        modifier: Modifier,
        names: List<String> = List(10) { "$it Aldi Nvidia" }
    ) {
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            items(items = names) { name ->
                Greeting(name = name)
            }
        }

    }
}