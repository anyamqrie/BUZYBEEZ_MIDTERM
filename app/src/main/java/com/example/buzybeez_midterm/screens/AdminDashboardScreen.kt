package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
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
fun AdminDashboardScreen(
    viewModel: AppViewModel,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDFBF5)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column {
                    Text("Admin Panel", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = BeeDark)
                    Text("Worker Verification Requests", style = MaterialTheme.typography.bodyMedium, color = BeeGray)
                }
                TextButton(onClick = { viewModel.logout(); onLogout() }) {
                    Text("Log Out", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Simulated list of verification requests
            val requests = listOf(
                VerificationItem("usr-882", "Albert Moss", "Passport", "PENDING"),
                VerificationItem("usr-911", "Melchora Aquino", "PhilSys", "PENDING"),
                VerificationItem("usr-042", "Narda Custodio", "UMID", "PENDING")
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(requests) { item ->
                    VerificationRequestCard(item)
                }
            }
        }
    }
}

@Composable
fun VerificationRequestCard(item: VerificationItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BeeLightGray)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(Modifier.size(48.dp), shape = CircleShape, color = BeeLightGray) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, tint = BeeGray) }
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(item.name, fontWeight = FontWeight.Bold, color = BeeDark)
                    Text("ID: ${item.userId} • ${item.idType}", style = MaterialTheme.typography.bodySmall, color = BeeGray)
                }
                Spacer(Modifier.weight(1f))
                Surface(color = Color(0xFFFFF9E6), shape = RoundedCornerShape(8.dp)) {
                    Text(item.status, modifier = Modifier.padding(6.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BeeYellow)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { /* Approve */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6F4EA)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, null, tint = Color(0xFF1E8E3E), modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Approve", color = Color(0xFF1E8E3E), fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = { /* Reject */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE7E6)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.Red, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Reject", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

data class VerificationItem(val userId: String, val name: String, val idType: String, val status: String)
