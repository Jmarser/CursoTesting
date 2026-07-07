package com.jmarser.cursotesting.core.presentation.components


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.MARKET_TOP_APP_BAR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.MARKET_TOP_APP_BAR_BACK_BUTTON

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackSelected: () -> Unit
) {

    TopAppBar(
        modifier = modifier.testTag(MARKET_TOP_APP_BAR),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.testTag(MARKET_TOP_APP_BAR_BACK_BUTTON),
                onClick = {onBackSelected()}
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

