package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeYellow
import com.example.buzybeez_midterm.ui.theme.BUZYBEEZ_MIDTERMTheme

@Composable
fun WelcomeScreen(onNavigateToLogin: () -> Unit, onNavigateToSignUp: () -> Unit) {
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Buzy Beez",
                    style = MaterialTheme.typography.titleLarge,
                    color = BeeDark
                )
                Text(text = "🐝", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Main Content
            Text(
                text = "Welcome to\nBuzy Beez!",
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                color = BeeDark,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your trusted way to find help\nfor everyday tasks.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = BeeDark.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bee Illustration Placeholder
            Text(text = "🐝", fontSize = 120.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Find the right worker.\nGet the job done.",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = BeeDark
            )

            Spacer(modifier = Modifier.weight(1f))

            // Buttons
            Button(
                onClick = { onNavigateToSignUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
            ) {
                Text(
                    text = "Create an Account",
                    style = MaterialTheme.typography.labelLarge,
                    color = BeeDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BeeDark)
            ) {
                Text(
                    text = "Log In",
                    style = MaterialTheme.typography.labelLarge,
                    color = BeeDark
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Text(
                text = "Safe • Simple • Local",
                style = MaterialTheme.typography.bodyMedium,
                color = BeeDark.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                Text(
                    text = "Buzy Beez | Est. 2026",
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = BeeDark.copy(alpha = 0.4f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    BUZYBEEZ_MIDTERMTheme {
        WelcomeScreen(onNavigateToLogin = {}, onNavigateToSignUp = {})
    }
}
