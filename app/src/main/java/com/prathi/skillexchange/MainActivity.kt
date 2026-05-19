package com.prathi.skillexchange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.prathi.skillexchange.navigation.AppNavigation
import com.prathi.skillexchange.ui.theme.SkillExchangeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkillExchangeTheme {
                AppNavigation()
            }
        }
    }
}