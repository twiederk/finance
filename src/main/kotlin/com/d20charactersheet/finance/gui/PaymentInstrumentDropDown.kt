package com.d20charactersheet.finance.gui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.d20charactersheet.finance.domain.PaymentInstrument

@Composable
fun PaymentInstrumentDropDown(
    viewModel: MoneyTransferViewModel,
    categories: List<PaymentInstrument>,
) {
    val isOpen = remember { mutableStateOf(false) }

    Box {
        Column {
            val color = DataColorSelector().getPaymentInstrumentColor(viewModel.paymentInstrument.value)
            Surface(color = color) {
                TextField(
                    value = viewModel.paymentInstrument.value.name,
                    onValueChange = { },
                    label = { Text("Payment Instrument") }
                )
            }

            DropdownMenu(
                expanded = isOpen.value,
                onDismissRequest = { isOpen.value = false }
            ) {
                categories.forEach {
                    DropdownMenuItem(
                        onClick = {
                            isOpen.value = false
                            viewModel.onPaymentInstrumentChange(it)
                        }
                    ) {
                        Text(it.name)
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .padding(10.dp)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}
