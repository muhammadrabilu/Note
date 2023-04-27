package com.rabilu.note.presentation

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Testing() {
    val color = remember {
        mutableStateOf(Color.Red)
    }

    Column(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .background(Color.Blue, shape = MaterialTheme.shapes.large)
                .padding(16.dp),
            text = "Hello world!",
            style = TextStyle(
                color = color.value,
                fontSize = 20.sp
            )
        )

        Button(onClick = { color.value = Color.Green }) {
            Text(text = "Click me")
        }
    }
}


@Preview
@Composable
fun showTesting() {
    Testing()
}