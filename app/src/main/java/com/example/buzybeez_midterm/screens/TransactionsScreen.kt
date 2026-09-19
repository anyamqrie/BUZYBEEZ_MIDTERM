package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.models.BookingModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun TransactionsScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val bookings by viewModel.bookings.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // Filter bookings for the current user
    val userTransactions = bookings.filter { it.customerId == currentUser?.userId }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDFBF5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BeeDark)
                }
                Column {
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = BeeDark
                    )
                    Text(
                        text = "Your recent activity",
                        style = MaterialTheme.typography.bodySmall,
                        color = BeeGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "All transactions", fontWeight = FontWeight.Bold, color = BeeDark)
                TextButton(onClick = { /* TODO */ }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Filter", color = BeeGray)
                        Icon(Icons.Default.ArrowDropDown, null, tint = BeeGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (userTransactions.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = "No transactions yet.", color = BeeGray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(userTransactions) { booking ->
                        TransactionItem(booking)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
            ) {
                Text(text = "View payment history", color = BeeDark, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TransactionItem(booking: BookingModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BeeLightGray)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = booking.scheduledStartTime.split(" ").getOrNull(0) ?: "Date", color = BeeGray, fontSize = 12.sp)
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Worker ID: ${booking.helperId ?: "N/A"}", fontWeight = FontWeight.Bold, color = BeeDark)
                    Text(text = "Booking", color = BeeGray, fontSize = 12.sp)
                }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BeeLightGray)
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "₱${booking.totalFare}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = BeeDark)
                Surface(
                    color = if (booking.status == "COMPLETED") Color(0xFFE6F4EA) else Color(0xFFFFF9E6),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = booking.status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = if (booking.status == "COMPLETED") Color(0xFF1E8E3E) else BeeYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
