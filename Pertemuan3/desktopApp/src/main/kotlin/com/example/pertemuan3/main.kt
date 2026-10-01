package com.example.pertemuan3

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pertemuan3",
    ) {
        App()
    }
}