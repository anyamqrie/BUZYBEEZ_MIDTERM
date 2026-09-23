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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.models.HelperModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun WorkersScreen(
    viewModel: AppViewModel,
    onNavigateToDashboard: () -> Unit,
    onNavigateToWorkerProfile: () -> Unit
) {
    val helpers by viewModel.helpers.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDFBF5) // Soft beige consistency
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Available Workers",
                    style = MaterialTheme.typography.displayLarge,
                    color = BeeDark
                )
                TextButton(onClick = onNavigateToDashboard) {
                    Text("Worker View", color = BeeYellow, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(helpers) { helper ->
                    WorkerItem(
                        name = helper.fullName,
                        status = helper.availabilityStatus,
                        phone = helper.mobileNumber
                    ) {
                        viewModel.selectWorker(helper)
                        onNavigateToWorkerProfile()
                    }
                }
            }
        }
    }
}

@Composable
fun WorkerItem(name: String, status: String, phone: String, onHire: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BeeDark
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (status == "ONLINE") BeeYellow else BeeDark.copy(alpha = 0.6f)
                )
                Text(
                    text = phone,
                    style = MaterialTheme.typography.bodySmall,
                    color = BeeDark.copy(alpha = 0.5f)
                )
            }
            Button(
                onClick = onHire,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
            ) {
                Text("Hire", color = BeeDark)
            }
        }
    }
}
