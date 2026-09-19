package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.models.BookingModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun CalendarScreen(
    viewModel: AppViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val userRole = currentUser?.role ?: "customer"
    val userId = currentUser?.userId ?: ""

    // Filter appointments based on role
    val appointments = if (userRole == "worker") {
        bookings.filter { it.helperId == userId || it.status == "PENDING" } // Workers see their jobs or unassigned pending
    } else {
        bookings.filter { it.customerId == userId } // Customers see their requests
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDFBF5)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text(
                text = "Calendar",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = BeeDark
            )
            Text(
                text = if (userRole == "worker") "Your upcoming jobs and requests" else "Your scheduled appointments",
                style = MaterialTheme.typography.bodyMedium,
                color = BeeGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BeeYellow)
                }
            } else if (appointments.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No appointments found.", color = BeeGray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(appointments) { booking ->
                        AppointmentCard(booking, userRole)
                    }
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(booking: BookingModel, userRole: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BeeLightGray)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(40.dp), shape = CircleShape, color = BeeLightGray) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, tint = BeeGray) }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (userRole == "worker") "Customer Request" else "Service Appointment",
                        fontWeight = FontWeight.Bold,
                        color = BeeDark
                    )
                    Text(
                        text = if (userRole == "worker") "From ID: ${booking.customerId}" else "Worker ID: ${booking.helperId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BeeGray
                    )
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    color = if (booking.status == "PENDING") Color(0xFFFFF9E6) else Color(0xFFE6F4EA),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = booking.status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = if (booking.status == "PENDING") BeeYellow else Color(0xFF1E8E3E),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BeeLightGray)
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null, tint = BeeYellow, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(booking.scheduledStartTime, style = MaterialTheme.typography.bodySmall, color = BeeDark)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, null, tint = BeeYellow, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("₱${booking.totalFare} • ${booking.paymentMethod}", style = MaterialTheme.typography.bodySmall, color = BeeDark)
            }
        }
    }
}
