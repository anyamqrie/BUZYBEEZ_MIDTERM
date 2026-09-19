package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun SignUpScreen(
    viewModel: AppViewModel,
    onSignUpSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("customer") } // customer, worker, admin

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Back Button
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = BeeDark)
                }
            }

            Text(
                text = "Join Buzy Beez",
                style = MaterialTheme.typography.displayLarge,
                color = BeeDark,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Create an account to get started.",
                style = MaterialTheme.typography.bodyMedium,
                color = BeeDark.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Role Selection
            Text(text = "I WANT TO BE A...", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BeeGray, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RoleCard("Customer", "Find help", selectedRole == "customer", Modifier.weight(1f)) { selectedRole = "customer" }
                RoleCard("Worker", "Offer help", selectedRole == "worker", Modifier.weight(1f)) { selectedRole = "worker" }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form
            SignUpField(label = "FIRST NAME", value = firstName, onValueChange = { firstName = it }, icon = Icons.Default.Person)
            SignUpField(label = "LAST NAME", value = lastName, onValueChange = { lastName = it }, icon = Icons.Default.Person)
            SignUpField(label = "EMAIL", value = email, onValueChange = { email = it }, icon = Icons.Default.Email)
            SignUpField(label = "PHONE NUMBER", value = phoneNumber, onValueChange = { phoneNumber = it }, icon = Icons.Default.Phone)
            SignUpField(label = "PASSWORD", value = password, onValueChange = { password = it }, icon = Icons.Default.Lock, isPassword = true)

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    viewModel.prepareSignup(firstName, lastName, email, phoneNumber, password, selectedRole)
                    onSignUpSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow),
                enabled = firstName.isNotBlank() && email.isNotBlank() && password.length >= 6
            ) {
                Text(text = "Continue", style = MaterialTheme.typography.labelLarge, color = BeeDark)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                Text(text = "Buzy Beez | Est. 2026", fontSize = 10.sp, color = BeeDark.copy(alpha = 0.4f))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RoleCard(title: String, desc: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) BeeYellow else Color.White),
        border = if (!isSelected) BorderStroke(1.dp, BeeLightGray) else null
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.Center) {
            Text(text = title, fontWeight = FontWeight.Bold, color = BeeDark, fontSize = 14.sp)
            Text(text = desc, fontSize = 10.sp, color = if (isSelected) BeeDark else BeeGray)
        }
    }
}

@Composable
fun SignUpField(label: String, value: String, onValueChange: (String) -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector, isPassword: Boolean = false) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BeeGray, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(icon, contentDescription = null, tint = BeeDark) },
            visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BeeYellow, unfocusedBorderColor = BeeLightGray)
        )
    }
}
