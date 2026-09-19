package com.example.buzybeez_midterm.screens

import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeLightGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

@Composable
fun WorkerProfileScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onHireSuccess: () -> Unit
) {
    val workerState = viewModel.selectedWorker.collectAsState()
    val worker = workerState.value
    val isLoading by viewModel.isLoading.collectAsState()
    
    var serviceAddress by remember { mutableStateOf("") }
    // Default coordinates: Bacolod City, Philippines
    var lat by remember { mutableDoubleStateOf(10.6765) }
    var lon by remember { mutableDoubleStateOf(122.9509) }

    if (worker == null) return

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(BeeYellow.copy(alpha = 0.2f))
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = BeeDark)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-40).dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = BeeLightGray,
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(text = "🐝", fontSize = 48.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(
                            text = worker.fullName,
                            style = MaterialTheme.typography.headlineMedium,
                            color = BeeDark
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = BeeYellow, modifier = Modifier.size(16.dp))
                            Text(text = " 4.9 (120 reviews)", style = MaterialTheme.typography.bodySmall, color = BeeGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Service Address",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BeeDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = serviceAddress,
                    onValueChange = { serviceAddress = it },
                    placeholder = { Text("Enter your street, city...", color = BeeGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = BeeYellow) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BeeYellow,
                        unfocusedBorderColor = BeeLightGray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Confirm Location on Map",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BeeDark
                )
                Text(
                    text = "Drag the pin to your exact location.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BeeGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    OSMMap(
                        latitude = lat, 
                        longitude = lon,
                        onLocationChanged = { newLat, newLon ->
                            lat = newLat
                            lon = newLon
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                InfoRow(label = "Base Rate", value = "₱500.00/hr")
                InfoRow(label = "Payment Method", value = "Cash on Delivery")
                InfoRow(label = "Coordinates", value = String.format("%.4f, %.4f", lat, lon))
                
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.bookWorker(
                            helperId = worker.helperId,
                            serviceId = "srv-demo",
                            address = "$serviceAddress ($lat, $lon)",
                            paymentMethod = "CASH",
                            onSuccess = onHireSuccess
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BeeYellow),
                    enabled = !isLoading && serviceAddress.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = BeeDark, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Hire Now (Cash Payment)",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 18.sp,
                            color = BeeDark
                        )
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = BeeGray)
        Text(text = value, fontWeight = FontWeight.Bold, color = BeeDark)
    }
}

@Composable
fun OSMMap(latitude: Double, longitude: Double, onLocationChanged: (Double, Double) -> Unit) {
    val html = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                #map { height: 100vh; width: 100vw; margin: 0; padding: 0; }
            </style>
        </head>
        <body style="margin:0;">
            <div id="map"></div>
            <script>
                var map = L.map('map', {zoomControl: false}).setView([$latitude, $longitude], 13);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19
                }).addTo(map);
                
                var marker = L.marker([$latitude, $longitude], {draggable: true}).addTo(map);
                
                marker.on('dragend', function(event) {
                    var position = marker.getLatLng();
                    Android.onLocationChanged(position.lat, position.lng);
                });

                map.on('click', function(e) {
                    marker.setLatLng(e.latlng);
                    Android.onLocationChanged(e.latlng.lat, e.latlng.lng);
                });
            </script>
        </body>
        </html>
    """.trimIndent()

    class MapInterface {
        @JavascriptInterface
        fun onLocationChanged(lat: Double, lon: Double) {
            onLocationChanged(lat, lon)
        }
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun onLocationChanged(lat: Double, lon: Double) {
                        onLocationChanged(lat, lon)
                    }
                }, "Android")
                loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            // We don't want to reload the map every time the coordinates change from the outside,
            // as the map itself is the source of truth for the manual move.
        },
        modifier = Modifier.fillMaxSize()
    )
}
