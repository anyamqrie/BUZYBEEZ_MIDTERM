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
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun CategoriesScreen(viewModel: AppViewModel) {
    val services by viewModel.services.collectAsState()

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
                text = "Service Categories",
                style = MaterialTheme.typography.displayLarge,
                color = BeeDark
            )
            Text(
                text = "Choose a service you need help with.",
                style = MaterialTheme.typography.bodyMedium,
                color = BeeDark.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(services) { service ->
                    ServiceItem(
                        name = service.serviceName,
                        category = service.category,
                        rate = service.baseRatePerHour
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceItem(name: String, category: String, rate: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BeeDark
                )
                Text(
                    text = "₱$rate/hr",
                    fontWeight = FontWeight.Bold,
                    color = BeeYellow
                )
            }
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall,
                color = BeeDark.copy(alpha = 0.6f)
            )
        }
    }
}
