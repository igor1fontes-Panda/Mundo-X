package com.example.mundoaberto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mundoaberto.ui.MainScreen
import com.example.mundoaberto.ui.theme.MundoAbertoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MundoAbertoTheme {
                MainScreen()
            }
        }
    }
}
