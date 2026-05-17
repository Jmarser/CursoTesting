package com.jmarser.cursotesting.settings.presentation

import app.cash.turbine.test
import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.core.utils.MainDispatcherRule
import com.jmarser.cursotesting.productlist.data.repository.FakeSettingsRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `given respository with values when viewmodel is intialized then ui state is updated`() = runTest(mainDispatcherRule.scheduler) {

        val settingsRepository = FakeSettingsRepository().apply {
            setInStockOnly(true)
        }

        val viewModel = SettingsViewModel(settingsRepository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.inStockOnly)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `given viewmodel when theme mode is changed then ui stated and repository are update`() = runTest(mainDispatcherRule.scheduler) {

        val settingsRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(settingsRepository)

        viewModel.uiState.test {
            awaitItem()

            viewModel.setThemeMode(ThemeMode.DARK)

            val updateState = awaitItem()

            assertEquals(ThemeMode.DARK, updateState.themeMode)
            assertEquals(ThemeMode.DARK, settingsRepository.themeMode.first() )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `given viewmodel when in stock only is changed then ui stated and repository are update`() = runTest(mainDispatcherRule.scheduler) {

        val settingsRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(settingsRepository)

        viewModel.uiState.test {
            awaitItem()

            viewModel.setInStockOnly(true)

            val updateState = awaitItem()

            assertEquals(true, updateState.inStockOnly)
            assertEquals(true, settingsRepository.inStockOnly.first() )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `given viewModel when repository change externally when ui state update automatically`() = runTest(mainDispatcherRule.scheduler) {

        val settingsRepository = FakeSettingsRepository()
        val viewModel = SettingsViewModel(settingsRepository)

        viewModel.uiState.test {
            awaitItem()

            settingsRepository.setInStockOnly(true)

            assertTrue(awaitItem().inStockOnly)

            cancelAndConsumeRemainingEvents()
        }
    }

}