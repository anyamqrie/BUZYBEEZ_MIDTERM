package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun PaymentScreen(
    viewModel: AppViewModel,
    onPaymentComplete: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf("CASH") }
    val worker = viewModel.selectedWorker.collectAsState().value
    val isLoading by viewModel.isLoading.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDFBF5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = BeeDark)
            }
            
            Text("Payment Method", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = BeeDark)
            Text("Choose how you'd like to pay.", style = MaterialTheme.typography.bodyMedium, color = BeeGray)

            Spacer(modifier = Modifier.height(32.dp))

            PaymentMethodCard("Cash on Delivery", "Pay after service", Icons.Default.Payments, selectedMethod == "CASH") { selectedMethod = "CASH" }
            PaymentMethodCard("GCash / E-Wallet", "Fast and secure", Icons.Default.Wallet, selectedMethod == "GCASH") { selectedMethod = "GCASH" }
            PaymentMethodCard("Credit / Debit Card", "Visa or Mastercard", Icons.Default.CreditCard, selectedMethod == "CARD") { selectedMethod = "CARD" }

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BeeLightGray)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Summary", fontWeight = FontWeight.Bold, color = BeeDark)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Service Fee", color = BeeGray)
                        Text("₱500.00", fontWeight = FontWeight.Bold, color = BeeDark)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Total", color = BeeDark, fontWeight = FontWeight.Bold)
                        Text("₱500.00", fontWeight = FontWeight.ExtraBold, color = BeeDark, fontSize = 20.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (worker != null) {
                        viewModel.bookWorker(worker.helperId, "srv-demo", "Bacolod City", selectedMethod, onPaymentComplete)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = BeeDark, modifier = Modifier.size(24.dp))
                } else {
                    Text("Confirm & Hire", style = MaterialTheme.typography.labelLarge, color = BeeDark)
                }
            }
        }
    }
}

@Composable
fun PaymentMethodCard(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFFFF9E6) else Color.White),
        border = BorderStroke(1.dp, if (isSelected) BeeYellow else BeeLightGray)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = if (isSelected) BeeYellow else BeeLightGray) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = if (isSelected) Color.White else BeeGray) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = BeeDark)
                Text(desc, style = MaterialTheme.typography.bodySmall, color = BeeGray)
            }
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = BeeYellow))
        }
    }
}
