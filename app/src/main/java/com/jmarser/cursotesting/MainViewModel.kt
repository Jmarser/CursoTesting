package com.jmarser.cursotesting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Project: CursoTesting
 * File: MainViewModel.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 19/03/2026
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsRepository: SettingsRepository
): ViewModel() {

    val themeMode: Flow<ThemeMode> = settingsRepository.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )
}