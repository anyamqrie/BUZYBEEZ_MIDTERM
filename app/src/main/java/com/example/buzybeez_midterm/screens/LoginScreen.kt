package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow
import androidx.compose.ui.graphics.Color

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = BeeDark)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.displayLarge,
                color = BeeDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Log in to continue to your Buzy Beez account.",
                style = MaterialTheme.typography.bodyMedium,
                color = BeeDark.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
            
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
                    placeholder = { Text("Enter your username", color = BeeGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Text("@", color = BeeDark, fontSize = 18.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BeeYellow,
                        unfocusedBorderColor = BeeLightGray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "PASSWORD",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BeeGray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Enter your password", color = BeeGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Text("•", color = BeeDark, fontSize = 24.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BeeYellow,
                        unfocusedBorderColor = BeeLightGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // BYPASS LOGIN FOR DEMO
            Button(
                onClick = { 
                    // Simulate success immediately for demo
                    viewModel.login(username, password, onLoginSuccess) 
                    onLoginSuccess() 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
            ) {
                Text(
                    text = "Log In",
                    style = MaterialTheme.typography.labelLarge,
                    color = BeeDark
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "or continue with", style = MaterialTheme.typography.bodySmall, color = BeeGray)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                LoginSocialIcon("f")
                Spacer(modifier = Modifier.width(16.dp))
                LoginSocialIcon("G")
                Spacer(modifier = Modifier.width(16.dp))
                LoginSocialIcon("📱")
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account? ", color = BeeDark, fontSize = 14.sp)
                Text(
                    text = "Sign Up",
                    color = BeeDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}

@Composable
fun LoginSocialIcon(label: String) {
    OutlinedCard(
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BeeLightGray)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = label, fontSize = 20.sp, color = BeeDark)
        }
    }
}
