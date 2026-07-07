package com.jmarser.cursotesting.settings.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.jmarser.cursotesting.R
import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.MARKET_TOP_APP_BAR_BACK_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_CONTENT
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_IN_STOCK_SWITCH
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_SEGMENT_BUTTON
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_SEGMENT_BUTTON_OPTION_CLEAR
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_SEGMENT_BUTTON_OPTION_DARK
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_SEGMENT_BUTTON_OPTION_SYSTEM
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.SETTINGS_TAX_SWITCH
import com.jmarser.cursotesting.core.presentation.testing.UiTestTag.TOP_APP_BAR
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertTrue

class SettingsScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun createSettingsScreen(
        onBack: () -> Unit = {},
        uiState: SettingsUiState = SettingsUiState(),
        onInStockOnlyChange: (Boolean) -> Unit = {},
        onThemeMode: (ThemeMode) -> Unit = {}
    ){
        composeRule.setContent {
            SettingsContent (
                uiState = uiState,
                onBack = onBack,
                onInStockOnlyChange = onInStockOnlyChange,
                onThemeMode = onThemeMode
            )
        }
    }

    private fun getString(resId: Int): String = composeRule.activity.getString(resId)

    @Test
    fun firstUiTest() {
        createSettingsScreen()

        composeRule.onNodeWithText(getString(R.string.settings_title)).assertIsDisplayed()
    }

    @Test
    fun given_default_settings_state_when_redered_then_shows_filter_and_appearance_sections(){
        createSettingsScreen(uiState = SettingsUiState())

        composeRule.onNodeWithText(getString(R.string.settings_title)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.settings_filters_section)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.settings_in_stock_only_title)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.settings_in_stock_only_subtitle)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.settings_included_tax_title)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.settings_included_tax_subtitle)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.settings_appearance_section)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.settings_appearance_title)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.settings_appearance_subtitle)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.theme_system)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.theme_clear)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.theme_dark)).assertIsDisplayed()

        composeRule.onNodeWithTag(SETTINGS_CONTENT).assertIsDisplayed()
        composeRule.onNodeWithTag(SETTINGS_IN_STOCK_SWITCH).assertIsOff()
        composeRule.onNodeWithTag(SETTINGS_TAX_SWITCH).assertIsOn()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON).assertIsDisplayed()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_SYSTEM).assertIsSelected()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_CLEAR).assertIsNotSelected()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_DARK).assertIsNotSelected()
    }

    @Test
    fun given_in_stock_only_false_when_rendered_then_switch_isOff(){
        createSettingsScreen(uiState = SettingsUiState(inStockOnly = false))
        composeRule.onNodeWithTag(SETTINGS_IN_STOCK_SWITCH).assertIsOff()
    }

    @Test
    fun given_in_stock_only_true_when_rendered_then_switch_isOn(){
        createSettingsScreen(uiState = SettingsUiState(inStockOnly = true))
        composeRule.onNodeWithTag(SETTINGS_IN_STOCK_SWITCH).assertIsOn()
    }

    @Test
    fun given_appearance_default_when_rendered_then_system_option_is_selected(){
        createSettingsScreen(uiState = SettingsUiState())
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_SYSTEM).assertIsSelected()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_CLEAR).assertIsNotSelected()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_DARK).assertIsNotSelected()
    }


    @Test
    fun given_clear_theme_when_redered_then_clear_option_is_selected(){
        createSettingsScreen(uiState = SettingsUiState(themeMode = ThemeMode.LIGHT))
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_SYSTEM).assertIsNotSelected()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_CLEAR).assertIsSelected()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_DARK).assertIsNotSelected()
    }

    @Test
    fun given_dark_theme_when_redered_then_dark_option_is_selected(){
        createSettingsScreen(uiState = SettingsUiState(themeMode = ThemeMode.DARK))
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_SYSTEM).assertIsNotSelected()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_CLEAR).assertIsNotSelected()
        composeRule.onNodeWithTag(SETTINGS_SEGMENT_BUTTON_OPTION_DARK).assertIsSelected()
    }

    @Test
    fun given_settings_redered_when_back_clicked_then_emit_back_callback(){
        var backClicked = false
        createSettingsScreen(onBack = { backClicked = true })
        composeRule.onNodeWithTag(MARKET_TOP_APP_BAR_BACK_BUTTON).performClick()
        assertTrue(backClicked)
    }

    @Test
    fun given_in_stock_switch_off_when_clicked_then_emits_true(){
        var emitted: Boolean = false
        createSettingsScreen(
            uiState = SettingsUiState(inStockOnly = false),
            onInStockOnlyChange = {newState -> emitted = newState}
        )

        composeRule.onNodeWithTag(SETTINGS_IN_STOCK_SWITCH).performClick()

        assertTrue(emitted)
    }
}