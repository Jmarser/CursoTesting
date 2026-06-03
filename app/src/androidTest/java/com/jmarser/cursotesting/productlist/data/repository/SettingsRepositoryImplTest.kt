package com.jmarser.cursotesting.productlist.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.core.mockwebserver.MockWebServerUrlHolder
import com.jmarser.cursotesting.core.mockwebserver.rules.MockWebServerRule
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SettingsRepositoryImplTest {

    @get:Rule(order = 0)
    val mockWebServer = MockWebServerRule()

    @get:Rule(order = 1)
    val hilt = HiltAndroidRule(this)

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Before
    fun setUp() {
        hilt.inject()
    }

    @After
    fun tearDown() {
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }


    @Test
    fun given_no_data_saved_when_in_stockOnly_isRead_then_returns_default_false() = runTest {
        val result = settingsRepository.inStockOnly.first()

        assertFalse(result)
    }

    @Test
    fun given_repository_when_set_in_stock_only_to_true_then_persist_value() = runTest {
        settingsRepository.setInStockOnly(true)
        val result = settingsRepository.inStockOnly.first()

        assertTrue(result)
    }

    @Test
    fun given_no_data_saved_when_filter_visible_isRead_then_returns_default_true() = runTest {
        val result = settingsRepository.filtersVisible.first()

        assertTrue(result)
    }

    @Test
    fun given_repository_when_set_filter_visible_to_false_then_persist_value() = runTest {
        settingsRepository.setFiltersVisible(false)
        val result = settingsRepository.filtersVisible.first()

        assertFalse(result)
    }

    @Test
    fun given_no_data_saved_when_select_category_isRead_then_returns_default_null() = runTest {
        val result = settingsRepository.selectedCategory.first()

        assertNull(result)
    }

    @Test
    fun given_repository_when_set_selected_category_then_persist_value() = runTest {
        settingsRepository.setSelectedCategory("Tomate")
        val result = settingsRepository.selectedCategory.first()

        assertEquals("Tomate", result)
    }

    @Test
    fun given_no_data_saved_when_theme_mode_isRead_then_returns_default_system() = runTest {
        val result = settingsRepository.themeMode.first()

        assertEquals(ThemeMode.SYSTEM, result)
    }

    @Test
    fun given_repository_when_set_theme_mode_then_persist_value() = runTest {
        settingsRepository.setThemeMode(ThemeMode.DARK)
        val result = settingsRepository.themeMode.first()

        assertEquals(ThemeMode.DARK, result)
    }

    @Test
    fun given_no_data_saved_when_sort_options_isRead_then_returns_default_none() = runTest {
        val result = settingsRepository.sortOption.first()

        assertEquals(SortOption.NONE, result)
    }

    @Test
    fun given_repository_when_set_sort_options_then_persist_value() = runTest {
        settingsRepository.setSortOption(SortOption.PRICE_ASC)
        val result = settingsRepository.sortOption.first()

        assertEquals(SortOption.PRICE_ASC, result)
    }

    @Test
    fun given_multiple_settings_changes_when_readAll_then_state_is_consistent() = runTest {
        settingsRepository.setInStockOnly(true)
        settingsRepository.setFiltersVisible(false)
        settingsRepository.setSelectedCategory("Tomate")
        settingsRepository.setThemeMode(ThemeMode.LIGHT)
        settingsRepository.setSortOption(SortOption.PRICE_DESC)

        assertTrue(settingsRepository.inStockOnly.first())
        assertFalse(settingsRepository.filtersVisible.first())
        assertEquals("Tomate", settingsRepository.selectedCategory.first())
        assertEquals(ThemeMode.LIGHT, settingsRepository.themeMode.first())
        assertEquals(SortOption.PRICE_DESC, settingsRepository.sortOption.first())
    }
}