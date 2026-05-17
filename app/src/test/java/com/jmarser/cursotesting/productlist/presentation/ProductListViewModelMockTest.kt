package com.jmarser.cursotesting.productlist.presentation

import com.jmarser.cursotesting.cart.data.repository.FakeProductRepositoryImpl
import com.jmarser.cursotesting.core.data.util.FakeClock
import com.jmarser.cursotesting.core.utils.MainDispatcherRule
import com.jmarser.cursotesting.productlist.data.repository.FakePromotionRepository
import com.jmarser.cursotesting.productlist.data.repository.FakeSettingsRepository
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import com.jmarser.cursotesting.productlist.domain.repository.ProductRepository
import com.jmarser.cursotesting.productlist.domain.repository.SettingsRepository
import com.jmarser.cursotesting.productlist.domain.usecase.GetProductsUseCase
import com.jmarser.cursotesting.productlist.domain.usecase.GetPromotionForProduct
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ProductListViewModelMockTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val settingsRepository: SettingsRepository = mockk(relaxed = true){
        every { selectedCategory } returns flowOf(null)
        every { sortOption } returns flowOf(SortOption.NONE)
        every { inStockOnly } returns flowOf(false)
        every { filtersVisible } returns flowOf(false)
    }

    private fun createViewModel(
        fakeProduct: ProductRepository = FakeProductRepositoryImpl(),
        fakeSettings: FakeSettingsRepository = FakeSettingsRepository(),
        fakePromotion: FakePromotionRepository = FakePromotionRepository(),
        fakeClock: FakeClock = FakeClock()
    ): ProductListViewModel{
        val getProductUseCase = GetProductsUseCase(
        fakeProduct,
        fakePromotion,
            GetPromotionForProduct(),
            fakeSettings,
            fakeClock
        )

        return ProductListViewModel(
            getProductUseCase,
            settingsRepository
        )
    }

    @Test
    fun `given category when set category then delegates to settings repository`() = runTest(mainDispatcherRule.scheduler) {
        val viewModel = createViewModel()
        val category = "pasta"

        viewModel.setCategory(category)

        coVerify(exactly = 1){settingsRepository.setSelectedCategory(category)}
    }

    @Test
    fun `given sort option when set sort option then delegates to settings repository`() = runTest {
        val viewModel = createViewModel()
        val option = true

        viewModel.setFilterVisible(option)

        coVerify (exactly = 1){ settingsRepository.setFiltersVisible(option) }
    }
}