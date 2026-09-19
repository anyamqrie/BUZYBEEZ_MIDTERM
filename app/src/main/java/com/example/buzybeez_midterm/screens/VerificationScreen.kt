package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun VerificationScreen(
    type: VerificationType,
    onVerify: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val title = if (type == VerificationType.EMAIL) "Check your email" else "Verify your phone"
    val subtitle = if (type == VerificationType.EMAIL) 
        "We sent a verification code\nto your email." 
        else "We sent a verification code\nto your mobile number."
    val icon = if (type == VerificationType.EMAIL) "✉️" else "☎️"

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
            // Back Button
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = BeeDark)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Icon
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = BeeYellow.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = icon, fontSize = 48.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                color = BeeDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = BeeDark.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Form
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "VERIFICATION CODE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = BeeGray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = { /* Skip logic */ },
                    placeholder = { Text("Verification Code", color = BeeGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    readOnly = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BeeYellow,
                        unfocusedBorderColor = BeeLightGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Verify Button
            Button(
                onClick = onVerify,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.labelLarge,
                    color = BeeDark
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Skip link
            Text(
                text = "Skip for now",
                color = BeeDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onVerify() }
            )

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

enum class VerificationType {
    EMAIL, PHONE
}
