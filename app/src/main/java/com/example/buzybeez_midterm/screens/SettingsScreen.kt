package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

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
                        text = "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = BeeDark
                    )
                    Text(
                        text = "Manage your account",
                        style = MaterialTheme.typography.bodySmall,
                        color = BeeGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search settings...", color = BeeGray) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = BeeGray) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = BeeLightGray,
                    focusedBorderColor = BeeYellow
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Settings Options
            SettingsItem(icon = Icons.Default.Circle, title = "Profile", subtitle = "Personal details and profile photo", color = Color(0xFF8B4513))
            SettingsItem(icon = Icons.Default.RadioButtonUnchecked, title = "Notifications", subtitle = "Alerts, reminders, and messages")
            SettingsItem(icon = Icons.Default.Schedule, title = "Activity", subtitle = "Bookings and recent activity")
            SettingsItem(icon = Icons.Default.Diamond, title = "Privacy", subtitle = "Control what others can see")
            SettingsItem(icon = Icons.Default.RemoveCircleOutline, title = "Security", subtitle = "Password and account protection")
            SettingsItem(icon = Icons.Default.Info, title = "About", subtitle = "Buzy Beez information")

            Spacer(modifier = Modifier.height(32.dp))

            // Account Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "Account", fontWeight = FontWeight.Bold, color = BeeDark, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Add or switch accounts",
                        color = BeeDark,
                        modifier = Modifier.clickable { /* TODO */ }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Log out",
                        color = BeeDark,
                        modifier = Modifier.clickable {
                            viewModel.logout()
                            onLogout()
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String, color: Color = BeeGray) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, color = BeeDark, fontSize = 16.sp)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = BeeGray)
        }
    }
}
