package com.jmarser.cursotesting.core.presentation.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuantitySelector(
    modifier: Modifier = Modifier,
    quantity: String,
    canDecrease: Boolean,
    canIncrease: Boolean,
    onDecreaseSelected: () -> Unit,
    onIncreaseSelected: () -> Unit,
    increaseTestTag: String? = null,
    decreaseTesTag: String? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(36.dp)
                .then(decreaseTesTag?.let{Modifier.testTag(it)} ?: Modifier),
            enabled = canDecrease,
            onClick = {
                onDecreaseSelected()
            }
        ) {
            Icon(
                modifier =Modifier.size(20.dp),
                imageVector = Icons.Default.Remove,
                contentDescription = "Botón decrecer unidades"
            )
        }

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(32.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = quantity,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        IconButton(
            modifier = Modifier
                .size(36.dp)
                .then(increaseTestTag?.let{Modifier.testTag(it)} ?: Modifier),
            enabled = canIncrease,
            onClick = {
                onIncreaseSelected()
            }
        ) {
            Icon(
                modifier =Modifier.size(20.dp),
                imageVector = Icons.Default.Add,
                contentDescription = "Botón aumentar unidades"
            )
        }

    }
}


