package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.buzybeez_midterm.models.BookingModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun WorkerDashboardScreen(viewModel: AppViewModel) {
    val bookings by viewModel.bookings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    var selectedBooking by remember { mutableStateOf<BookingModel?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDFBF5)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header / Profile Section for Worker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Worker Dashboard",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = BeeDark
                    )
                    Text(
                        text = "Welcome, ${currentUser?.fullName ?: "Worker"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = BeeGray
                    )
                }
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    color = BeeYellow.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🐝", fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedBooking != null) {
                // Map View to Preview Client Location before accepting
                LocationPreviewView(
                    booking = selectedBooking!!,
                    onAccept = {
                        viewModel.acceptBooking(selectedBooking!!.bookingId ?: "")
                        selectedBooking = null
                    },
                    onBack = { selectedBooking = null }
                )
            } else {
                Text(
                    text = "Active Requests",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BeeDark
                )
                Spacer(modifier = Modifier.height(16.dp))

                val pendingBookings = bookings.filter { it.status == "PENDING" }

                if (isLoading && pendingBookings.isEmpty()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = BeeYellow)
                    }
                } else {
                    if (pendingBookings.isEmpty()) {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(text = "No pending bookings found.", color = BeeGray)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(pendingBookings) { booking ->
                                WorkerBookingItem(booking) {
                                    selectedBooking = booking
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationPreviewView(booking: BookingModel, onAccept: () -> Unit, onBack: () -> Unit) {
    val coords = extractCoordinates(booking.serviceAddress)
    val lat = coords?.first ?: 10.6765
    val lon = coords?.second ?: 122.9509
    val clientName = getClientName(booking.customerId)

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, BeeLightGray)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, tint = BeeYellow)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(text = clientName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = BeeDark)
                            Text(text = "Client ID: ${booking.customerId}", fontSize = 10.sp, color = BeeGray)
                        }
                    }
                    Text(
                        text = "₱${booking.totalFare}",
                        fontWeight = FontWeight.ExtraBold,
                        color = BeeYellow,
                        fontSize = 20.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BeeLightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = BeeYellow, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = booking.serviceAddress.substringBefore(" ("),
                        color = BeeDark,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Client Location", fontWeight = FontWeight.Bold, color = BeeDark)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            OSMMap(latitude = lat, longitude = lon, onLocationChanged = { _, _ -> })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BeeYellow)
            ) {
                Text("Back", color = BeeDark)
            }
            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
            ) {
                Text("Accept Client", color = BeeDark)
            }
        }
    }
}

@Composable
fun WorkerBookingItem(booking: BookingModel, onSeeLocation: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BeeLightGray)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column {
                    Text(text = getClientName(booking.customerId), fontWeight = FontWeight.Bold, color = BeeDark, fontSize = 16.sp)
                    Text(text = "Job ID: ${booking.bookingId}", fontSize = 10.sp, color = BeeGray)
                }
                Text("₱${booking.totalFare}", color = BeeYellow, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = BeeGray, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(text = booking.serviceAddress.substringBefore(" ("), color = BeeGray, fontSize = 12.sp)
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onSeeLocation,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
            ) {
                Text("See Location", color = BeeDark)
            }
        }
    }
}

fun getClientName(id: String): String {
    return when (id) {
        "usr-882" -> "Albert Moss"
        "usr-911" -> "Melchora Aquino"
        "usr-042" -> "Narda Custodio"
        else -> "Valued Client"
    }
}

fun extractCoordinates(address: String): Pair<Double, Double>? {
    val regex = """\(([^,]+),\s*([^)]+)\)""".toRegex()
    val match = regex.find(address)
    return if (match != null) {
        val lat = match.groupValues[1].toDoubleOrNull() ?: 10.6765
        val lon = match.groupValues[2].toDoubleOrNull() ?: 122.9509
        Pair(lat, lon)
    } else {
        null
    }
}
