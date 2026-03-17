package com.jmarser.cursotesting.productlist.presentation.components


import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmarser.cursotesting.productlist.domain.model.SortOption
import com.jmarser.cursotesting.productlist.presentation.ProductListUiState
import com.jmarser.cursotesting.ui.theme.MyAppTheme

@Composable
fun FiltersMenu(
    modifier: Modifier = Modifier,
    state: ProductListUiState.Success,
    onCategorySelected: (String?) -> Unit,
    onOrderSelected: (SortOption) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Categorías")
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.selectedCategory == null,
                    onClick = {
                        onCategorySelected(null)
                    },
                    label = {
                        Text(
                            text = "Todas",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )

                state.categories.forEach { category ->
                    FilterChip(
                        selected = category.equals(state.selectedCategory, ignoreCase = true),
                        onClick = {
                            onCategorySelected(category)
                        },
                        label = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }

            }
            HorizontalDivider()

            Text("Ordenar por")

            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = state.sortOption == SortOption.PRICE_ASC,
                    onClick = {
                        onOrderSelected(SortOption.PRICE_ASC)
                    },
                    label = {
                        Text(
                            text = "Precio asc.",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
                FilterChip(
                    selected = state.sortOption == SortOption.PRICE_DESC,
                    onClick = {
                        onOrderSelected(SortOption.PRICE_DESC)
                    },
                    label = {
                        Text(
                            text = "Precio desc.",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
                FilterChip(
                    selected = state.sortOption == SortOption.DISCOUNT,
                    onClick = {
                        onOrderSelected(SortOption.DISCOUNT)
                    },
                    label = {
                        Text(
                            text = "Descuento",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
        }
    }
}

//@Preview(
//    showSystemUi = true,
//    showBackground = true
//)
//@Composable
//fun FiltersMenuPreview() {
//    MyAppTheme() {
//        FiltersMenu(modifier = Modifier)
//    }
//}
