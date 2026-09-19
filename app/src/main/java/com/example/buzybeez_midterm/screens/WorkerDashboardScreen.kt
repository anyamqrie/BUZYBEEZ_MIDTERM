package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.models.BookingModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun WorkerDashboardScreen(viewModel: AppViewModel) {
    val bookings by viewModel.bookings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Worker Dashboard",
                style = MaterialTheme.typography.displayLarge,
                color = BeeDark
            )
            Text(
                text = "Accept incoming clients below.",
                style = MaterialTheme.typography.bodyMedium,
                color = BeeDark.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BeeYellow)
                }
            } else {
                val pendingBookings = bookings.filter { it.status == "PENDING" }
                
                if (pendingBookings.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No pending bookings found.", color = BeeGray)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(pendingBookings) { booking ->
                            BookingItem(booking) {
                                viewModel.acceptBooking(booking.bookingId ?: "")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingItem(booking: BookingModel, onAccept: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Service Request",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = BeeDark
                )
                Text(
                    text = "₱${booking.totalFare}",
                    fontWeight = FontWeight.Bold,
                    color = BeeYellow,
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Address: ${booking.serviceAddress}",
                fontSize = 14.sp,
                color = BeeDark.copy(alpha = 0.8f)
            )
            Text(
                text = "Time: ${booking.scheduledStartTime}",
                fontSize = 14.sp,
                color = BeeDark.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
            ) {
                Text(text = "Accept Client", color = BeeDark)
            }
        }
    }
}
