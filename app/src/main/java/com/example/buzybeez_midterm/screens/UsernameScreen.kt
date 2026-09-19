package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun UsernameScreen(
    viewModel: AppViewModel,
    onUsernameSet: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val authError by viewModel.authError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearAuthError()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Choose a username",
                style = MaterialTheme.typography.displayLarge,
                color = BeeDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This is how you'll be identified\nin Buzy Beez.",
                style = MaterialTheme.typography.bodyMedium,
                color = BeeDark.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "USERNAME",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BeeGray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("Enter your unique username", color = BeeGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Text("@", color = BeeDark, fontSize = 18.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BeeYellow,
                        unfocusedBorderColor = BeeLightGray
                    ),
                    enabled = !isLoading
                )
            }

            if (authError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = authError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { viewModel.completeSignup(username, onUsernameSet) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow),
                enabled = !isLoading && username.length >= 3
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = BeeDark)
                } else {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.labelLarge,
                        color = BeeDark
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                Text(
                    text = "Buzy Beez | Est. 2026",
                    fontSize = 10.sp,
                    color = BeeDark.copy(alpha = 0.4f)
                )
            }
        }
    }
}
