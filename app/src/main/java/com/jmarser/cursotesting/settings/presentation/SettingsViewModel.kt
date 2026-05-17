package com.jmarser.cursotesting.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: SettingsViewModel.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 18/03/2026
 */

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    //private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.inStockOnly,
        settingsRepository.themeMode
    ) { inStockOnly, themeMode ->
        SettingsUiState(inStockOnly = inStockOnly, themeMode = themeMode)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

/*    init {
        loadSettings()
    }

    fun loadSettings() {

    }*/

    fun setInStockOnly(newState: Boolean) {
        viewModelScope.launch {
            viewModelScope.launch {
                settingsRepository.setInStockOnly(newState)
            }
        }
    }

    fun setThemeMode(theme: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(theme)
        }
    }
}