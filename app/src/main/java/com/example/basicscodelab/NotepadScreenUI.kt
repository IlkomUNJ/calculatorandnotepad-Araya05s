package com.example.basicscodelab

import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

object NotepadScreenUI {
    @Composable
    fun setupLayout(navController: NavHostController) {

        Row(
            modifier = Modifier
                .background(Color(0xFF050505))
        ) {
            Column {
                Button(
                    onClick = {
                        // Navigate to the 'detail_screen' route
                        navController.navigate("home_screen")
                    },
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Text("Go to Home Screen")
                }
                Spacer(modifier = Modifier.height(4.dp))
                NotepadScreenUI(
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    @Composable
    fun NotepadScreenUI(modifier : Modifier
        ){
        val context: Context = LocalContext.current.applicationContext

        val viewModel: notepadScreenViewModel = viewModel(
            factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return notepadScreenViewModel(context as Application) as T
                }
            }
        )
            val textState by viewModel.textState.collectAsState()
            val currentSelection = textState.selection

        Column {
            Button(
                onClick = {
                    viewModel.saveText()
                },
                modifier = Modifier.padding(16.dp, 4.dp),
                colors = ButtonColors(Color.Magenta, Color.Black, Color.Magenta, Color.Black),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
            ) {
                Text("Save Note")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFFFFFFF),
                                Color.Transparent
                            )
                        )
                    )
            )
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    StyleControls(
                        onStyleAction = viewModel::applyStyle,
                        currentSelection = currentSelection
                    )
                }
            ) { paddingValues ->
                SelectionContainer(modifier = Modifier.padding(paddingValues)) {
                    TextField(
                        value = textState,
                        onValueChange = { viewModel.onTextChange(it) },
                        label = { Text("") },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = DEFAULT_FONT_SIZE_SP,
                            fontFamily = FIXED_FONT_FAMILY,
                            color = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF0A0606)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                        )

                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFFFFFFFF),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }
        }
        }
}
@Composable
fun StyleControls(
    onStyleAction: (FontStyleAction) -> Unit,
    currentSelection: TextRange,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Bold Button
        Button(onClick = { onStyleAction(FontStyleAction.ToggleBold(currentSelection)) }) {
            Text(
                "Bold",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }

        // Italic Button
        Button(onClick = { onStyleAction(FontStyleAction.ToggleItalic(currentSelection)) }) {
            Text(
                "Italic",
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Button(onClick = { onStyleAction(FontStyleAction.ChangeSize(currentSelection, DEFAULT_FONT_SIZE_SP.value+4f))}) {
            Text("Size +")
        }
        Button(onClick = { onStyleAction(FontStyleAction.ChangeSize(currentSelection, DEFAULT_FONT_SIZE_SP.value-4f))}) {
            Text("Size -")
        }
    }
}
