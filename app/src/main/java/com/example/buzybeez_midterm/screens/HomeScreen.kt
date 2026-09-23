package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onViewCategories: () -> Unit,
    onViewWorkers: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val helpers by viewModel.helpers.collectAsState()
    val services by viewModel.services.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFDFBF5) // Soft beige consistency
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Hello, 👋", style = MaterialTheme.typography.bodyMedium, color = BeeDark.copy(alpha = 0.6f))
                    Text(text = "Welcome Back!", style = MaterialTheme.typography.headlineMedium, color = BeeDark)
                }
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onNavigateToProfile() },
                    shape = RoundedCornerShape(12.dp),
                    color = BeeYellow.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🐝", fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Popular Services
            SectionHeader(title = "Popular Services", onSeeAll = onViewCategories)
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                   items(services.take(5)) { service ->
                    ServiceCard(service.serviceName)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Top Workers
            SectionHeader(title = "Top Workers Near You", onSeeAll = onViewWorkers)
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(helpers.take(5)) { helper ->
                    WorkerCard(helper.fullName, helper.availabilityStatus)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Promo Card
            Card(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BeeYellow)
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.CenterStart) {
                    Column {
                        Text(text = "Get 20% Off", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = BeeDark)
                        Text(text = "On your first booking!", fontSize = 14.sp, color = BeeDark.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, color = BeeDark)
        TextButton(onClick = onSeeAll) {
            Text(text = "See All", color = BeeYellow, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ServiceCard(name: String) {
    Card(
        modifier = Modifier.size(140.dp, 100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.BottomStart) {
            Text(text = name, fontWeight = FontWeight.Bold, color = BeeDark, fontSize = 14.sp)
        }
    }
}

@Composable
fun WorkerCard(name: String, status: String) {
    Card(
        modifier = Modifier.size(160.dp, 180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(16.dp),
                color = BeeLightGray
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🐝", fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = name, fontWeight = FontWeight.Bold, color = BeeDark, fontSize = 16.sp)
            Text(
                text = status, 
                fontSize = 12.sp, 
                color = if (status == "ONLINE") BeeYellow else BeeDark.copy(alpha = 0.5f)
            )
        }
    }
}
