package com.jmarser.cursotesting.productlist.presentation.components


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    filtersVisible: Boolean = false,
    cartItemCount: Int,
    onFiltersSelected: (Boolean) -> Unit,
    onSettingsSelected: () -> Unit,
    onNavigateToCart: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "MarketApp",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            IconButton(
                onClick = {
                    onFiltersSelected(!filtersVisible)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = if (filtersVisible) "Ocultar filtros" else "Mostrar filtros",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(
                onClick = {
                    onSettingsSelected()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ajustes",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            BadgedBox(
                modifier = Modifier
                    .padding(4.dp),
                badge = {
                    Text(
                        text = if(cartItemCount > 99) "99+" else cartItemCount.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            ) {             IconButton(
                onClick = {
                    onNavigateToCart()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }}
        }
    )
}


