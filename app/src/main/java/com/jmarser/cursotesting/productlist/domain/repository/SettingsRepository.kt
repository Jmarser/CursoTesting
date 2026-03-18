package com.jmarser.cursotesting.productlist.domain.repository

import com.jmarser.cursotesting.core.domain.model.ThemeMode
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import kotlinx.coroutines.flow.Flow

/**
 * Project: CursoTesting
 * File: SettingsRepository.kt
 * Author: Tu Jmarser <aenur32@gmail.com>
 * Created: 18/03/2026
 */

interface SettingsRepository {

    val inStockOnly: Flow<Boolean>
    val themeMode: Flow<ThemeMode>
    val selectedCategory: Flow<String?>
    val filtersVisible: Flow<Boolean>
    val sortOption: Flow<SortOption>

    suspend fun setInStockOnly(value: Boolean)
    suspend fun setThemeMode(value: ThemeMode)
    suspend fun setSelectedCategory(value: String?)
    suspend fun setFiltersVisible(value: Boolean)
    suspend fun setSortOption(value: SortOption)

}