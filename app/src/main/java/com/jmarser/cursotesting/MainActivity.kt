package com.jmarser.cursotesting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.core.presentation.navigation.NavGraph
import com.jmarser.cursotesting.ui.theme.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val themeMode by mainViewModel.themeMode.collectAsStateWithLifecycle(
                initialValue = ThemeMode.SYSTEM
            )

            val darkTheme = when(themeMode){
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            MyAppTheme(darkTheme = darkTheme) {
                NavGraph()
            }
        }
    }
}
