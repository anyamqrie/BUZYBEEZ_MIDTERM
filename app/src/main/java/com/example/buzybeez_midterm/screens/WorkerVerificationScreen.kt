package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WorkerVerificationScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onVerificationComplete: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    var currentIdDetailIndex by remember { mutableStateOf(0) }

    val idTypes = listOf(
        IdTypeItem("Passport", Icons.Default.Public),
        IdTypeItem("PhilSys", Icons.Default.Badge),
        IdTypeItem("Driver's License", Icons.Default.CreditCard),
        IdTypeItem("UMID", Icons.Default.ContactPage),
        IdTypeItem("Postal ID", Icons.Default.Portrait)
    )

    val selectedIdList = selectedIds.toList()

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
            if (step == 1) {
                VerificationHeader(onNavigateBack)
                Spacer(modifier = Modifier.height(32.dp))
                IdSelectionStep(
                    idTypes = idTypes,
                    selectedIds = selectedIds,
                    onToggleId = { name ->
                        selectedIds = if (selectedIds.contains(name)) selectedIds - name else selectedIds + name
                    },
                    onConfirm = { step = 3 }
                )
            } else if (step == 3) {
                val currentIdName = selectedIdList.getOrNull(currentIdDetailIndex) ?: ""
                IdDetailsStep(
                    idName = currentIdName,
                    onBack = {
                        if (currentIdDetailIndex > 0) currentIdDetailIndex-- else step = 1
                    },
                    onSave = {
                        if (currentIdDetailIndex < selectedIdList.size - 1) {
                            currentIdDetailIndex++
                        } else {
                            onVerificationComplete()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun VerificationHeader(onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BeeDark)
        }
        Column {
            Text(
                text = "ID Verification",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BeeDark
            )
            Text(
                text = "Worker verification",
                style = MaterialTheme.typography.bodySmall,
                color = BeeGray
            )
        }
    }
}

@Composable
fun IdSelectionStep(
    idTypes: List<IdTypeItem>,
    selectedIds: Set<String>,
    onToggleId: (String) -> Unit,
    onConfirm: () -> Unit
) {
    Text(
        text = "Please verify your identity to continue.",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = BeeDark
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "To help keep our community safe, reliable, and trustworthy, we verify every worker.",
        style = MaterialTheme.typography.bodySmall,
        color = BeeGray
    )

    Spacer(modifier = Modifier.height(24.dp))
    HorizontalDivider(color = BeeLightGray)
    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = "STEP 1",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = BeeDark
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Please choose at least two (2) IDs to verify your age and identity.",
        style = MaterialTheme.typography.bodyMedium,
        color = BeeGray
    )

    Spacer(modifier = Modifier.height(32.dp))

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            idTypes.take(3).forEach { item ->
                IdCard(item = item, isSelected = selectedIds.contains(item.name), modifier = Modifier.weight(1f)) { onToggleId(item.name) }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Spacer(modifier = Modifier.weight(0.5f))
            idTypes.drop(3).forEach { item ->
                IdCard(item = item, isSelected = selectedIds.contains(item.name), modifier = Modifier.weight(1f)) { onToggleId(item.name) }
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }

    Spacer(modifier = Modifier.height(48.dp))

    Button(
        onClick = onConfirm,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BeeYellow),
        enabled = selectedIds.size >= 2
    ) {
        Text(text = "Confirm Selection", style = MaterialTheme.typography.labelLarge, color = BeeDark)
    }
}

@Composable
fun IdDetailsStep(
    idName: String,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var isScanning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Form states
    var field1 by remember { mutableStateOf("") }
    var field2 by remember { mutableStateOf("") }
    var field3 by remember { mutableStateOf("") }
    var field4 by remember { mutableStateOf("") }
    var field5 by remember { mutableStateOf("") }
    var field6 by remember { mutableStateOf("") }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BeeDark)
            }
            Column {
                Text(text = "BUZY BEEZ", style = MaterialTheme.typography.labelSmall, color = Color(0xFF8B4513), fontWeight = FontWeight.Bold)
                Text(text = "$idName Details", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = BeeDark)
                Text(text = "Worker verification", style = MaterialTheme.typography.bodySmall, color = BeeGray)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = BeeLightGray)
        Spacer(modifier = Modifier.height(24.dp))

        // Step Counter and Scan Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.background(BeeYellow.copy(alpha = 0.2f), RoundedCornerShape(16.dp)).padding(horizontal = 12.dp, vertical = 4.dp)) {
                Text(text = "STEP 3", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF8B4513))
            }

            TextButton(
                onClick = {
                    scope.launch {
                        isScanning = true
                        delay(2000) // Simulate scanning
                        field1 = "P" + (1000000..9999999).random().toString()
                        field2 = "Manila"
                        field3 = "Anya Santos"
                        field4 = "Female"
                        isScanning = false
                    }
                },
                enabled = !isScanning,
                colors = ButtonDefaults.textButtonColors(contentColor = BeeYellow)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.QrCodeScanner, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isScanning) "Scanning..." else "Scan ID")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BeeLightGray)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "$idName Details", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BeeDark)
                Text(text = "Please fill in the information exactly as it appears on your ID.", style = MaterialTheme.typography.bodySmall, color = BeeGray)

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFF9E6),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = BeeLightGray) {
                            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, tint = BeeGray) }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = idName.uppercase(), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BeeDark)
                            Text(text = "Input required details", fontSize = 12.sp, color = BeeGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Dynamic fields based on ID type
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    when (idName) {
                        "Passport" -> {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                DetailField(label = "Passport Number", value = field1, onValueChange = { field1 = it }, modifier = Modifier.weight(1f))
                                DetailField(label = "Place of Birth", value = field2, onValueChange = { field2 = it }, modifier = Modifier.weight(1f))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                DetailField(label = "Name", value = field3, onValueChange = { field3 = it }, modifier = Modifier.weight(1f))
                                DetailField(label = "Sex", value = field4, onValueChange = { field4 = it }, modifier = Modifier.weight(1f))
                            }
                        }
                        "PhilSys" -> {
                            DetailField(label = "Card Number", value = field1, onValueChange = { field1 = it }, modifier = Modifier.fillMaxWidth())
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                DetailField(label = "Name", value = field3, onValueChange = { field3 = it }, modifier = Modifier.weight(1f))
                                DetailField(label = "Sex", value = field4, onValueChange = { field4 = it }, modifier = Modifier.weight(1f))
                            }
                        }
                        else -> {
                            DetailField(label = "ID Number", value = field1, onValueChange = { field1 = it }, modifier = Modifier.fillMaxWidth())
                            DetailField(label = "Full Name", value = field3, onValueChange = { field3 = it }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("DOCUMENT UPLOAD", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = BeeGray)
                Spacer(modifier = Modifier.height(12.dp))

                var clearanceImage by remember { mutableStateOf<String?>(null) }

                Card(
                    onClick = { clearanceImage = "uploaded_uri" },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                    border = BorderStroke(1.dp, BeeLightGray)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (clearanceImage == null) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CloudUpload, null, tint = BeeYellow)
                                Text("Upload Barangay/NBI Clearance", style = MaterialTheme.typography.bodySmall, color = BeeGray)
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF1E8E3E))
                                Spacer(Modifier.width(8.dp))
                                Text("Clearance Uploaded", fontWeight = FontWeight.Bold, color = BeeDark)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onSave,
                    modifier = Modifier.align(Alignment.CenterHorizontally).width(180.dp).height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
                ) {
                    Text(text = "Save & Continue", color = BeeDark, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Buzy Beez • Trusted workers, made simple.",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = BeeGray
        )
    }
}

@Composable
fun DetailField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = BeeGray, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BeeLightGray,
                focusedBorderColor = BeeYellow
            ),
            textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun IdCard(
    item: IdTypeItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) BeeYellow else Color.White),
        border = if (!isSelected) BorderStroke(1.dp, BeeLightGray) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(20.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, BeeDark),
                shape = RoundedCornerShape(2.dp)
            ) {
                if (isSelected) {
                    Box(modifier = Modifier.fillMaxSize().padding(4.dp).background(BeeDark))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Icon(imageVector = item.icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = BeeDark)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.name, style = TextStyle(fontSize = 10.sp), fontWeight = FontWeight.Medium, color = BeeDark, textAlign = TextAlign.Center)
        }
    }
}

data class IdTypeItem(val name: String, val icon: ImageVector)
