package com.jmarser.cursotesting.settings.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.core.presentation.components.MarketTopAppBar
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_CONTENT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_IN_STOCK_SWITCH
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_SEGMENT_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_SEGMENT_BUTTON_OPTION_CLEAR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_SEGMENT_BUTTON_OPTION_DARK
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_SEGMENT_BUTTON_OPTION_SYSTEM
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_TAX_SWITCH
import com.jmarser.cursotesting.ui.theme.MyAppTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsContent(
        modifier = Modifier,
        onBack = onBack,
        uiState = uiState,
        onInStockOnlyChange = {newState ->
            viewModel.setInStockOnly(newState)
        },
        onThemeMode = {themeMode ->
            viewModel.setThemeMode(themeMode)
        }
    )


}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    uiState: SettingsUiState = SettingsUiState(),
    onInStockOnlyChange: (Boolean) -> Unit = {},
    onThemeMode: (ThemeMode) -> Unit
) {
    Scaffold(
        topBar = {
            MarketTopAppBar(
                title = stringResource(R.string.settings_title),
                onBackSelected = { onBack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
                .testTag(SETTINGS_CONTENT),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = stringResource(R.string.settings_filters_section),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider()

                    // Opción Stock
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.settings_in_stock_only_title),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = stringResource(R.string.settings_in_stock_only_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            modifier = Modifier.testTag(SETTINGS_IN_STOCK_SWITCH),
                            checked = uiState.inStockOnly,
                            onCheckedChange = onInStockOnlyChange
                        )
                    }

                    HorizontalDivider()

                    // Opción Impuestos
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.settings_included_tax_title),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = stringResource(R.string.settings_included_tax_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            modifier = Modifier.testTag(SETTINGS_TAX_SWITCH),
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = stringResource(R.string.settings_appearance_section),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.settings_appearance_title),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = stringResource(R.string.settings_appearance_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                            SingleChoiceSegmentedButtonRow(
                                Modifier
                                    .fillMaxWidth()
                                    .testTag(SETTINGS_SEGMENT_BUTTON)
                            ) {
                                SegmentedButton(
                                    modifier = Modifier.testTag(SETTINGS_SEGMENT_BUTTON_OPTION_SYSTEM),
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = 0,
                                        count = 3
                                    ),
                                    onClick = {
                                        onThemeMode(ThemeMode.SYSTEM)
                                    },
                                    selected = uiState.themeMode == ThemeMode.SYSTEM,
                                    label = {
                                        Text(stringResource(R.string.theme_system))
                                    }
                                )
                                SegmentedButton(
                                    modifier = Modifier.testTag(SETTINGS_SEGMENT_BUTTON_OPTION_CLEAR),
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = 1,
                                        count = 3
                                    ),
                                    onClick = {
                                        onThemeMode(ThemeMode.LIGHT)
                                    },
                                    selected = uiState.themeMode == ThemeMode.LIGHT,
                                    label = {
                                        Text(stringResource(R.string.theme_clear))
                                    }
                                )
                                SegmentedButton(
                                    modifier = Modifier.testTag(SETTINGS_SEGMENT_BUTTON_OPTION_DARK),
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = 2,
                                        count = 3
                                    ),
                                    onClick = {
                                        onThemeMode(ThemeMode.DARK)
                                    },
                                    selected = uiState.themeMode == ThemeMode.DARK,
                                    label = {
                                        Text(stringResource(R.string.theme_dark))
                                    },
                                    colors = SegmentedButtonDefaults.colors(
                                        activeContainerColor = MaterialTheme.colorScheme.primary,
                                        disabledActiveContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        disabledInactiveContainerColor = MaterialTheme.colorScheme.error
                                    )
                                )
                            }
                        }

                    }

                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SettingsContentPreview(){
    MyAppTheme{
        SettingsContent(
            uiState = SettingsUiState(),
            onBack = {},
            onInStockOnlyChange = {},
            onThemeMode = {}
        )
    }
}

