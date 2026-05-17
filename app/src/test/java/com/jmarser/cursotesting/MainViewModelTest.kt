package com.jmarser.cursotesting

import androidx.lifecycle.viewmodel.compose.viewModel
import app.cash.turbine.test
import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.core.utils.MainDispatcherRule
import com.jmarser.cursotesting.productlist.data.repository.FakeSettingsRepository
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel(fakeSettingsRepository: SettingsRepository = FakeSettingsRepository()): MainViewModel{
       return MainViewModel(fakeSettingsRepository)
    }

    @Test
    fun `given repository with dark mode when initialized the emits dark theme mode`() = runTest(mainDispatcherRule.scheduler) {
        val settingsRepository = FakeSettingsRepository().apply {
            setThemeMode(ThemeMode.DARK)
        }
            val viewModel = createViewModel(settingsRepository)

        viewModel. themeMode.test {
            assertEquals(ThemeMode.DARK, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `given default reposiotry when initialized then emits system theme modo`() = runTest(mainDispatcherRule.scheduler) {
        val viewModel = createViewModel()

        viewModel.themeMode.test{
            assertEquals(ThemeMode.SYSTEM, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}