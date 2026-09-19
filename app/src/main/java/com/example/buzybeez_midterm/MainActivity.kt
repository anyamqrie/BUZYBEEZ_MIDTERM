package com.example.buzybeez_midterm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buzybeez_midterm.screens.*
import com.example.buzybeez_midterm.ui.theme.BUZYBEEZ_MIDTERMTheme
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeYellow

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BUZYBEEZ_MIDTERMTheme {
                BuzyBeezApp(viewModel)
            }
        }
    }
}

@Composable
fun BuzyBeezApp(viewModel: AppViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define which screens should show the bottom bar
    val screensWithBottomBar = listOf("home", "categories", "workers", "profile", "worker_verification", "transactions", "settings", "calendar")

    Scaffold(
        bottomBar = {
            if (currentRoute in screensWithBottomBar) {
                CustomBottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToSignUp = { navController.navigate("signup") }
                )
            }
            composable("login") {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        val role = viewModel.currentUser.value?.role ?: "customer"
                        when (role) {
                            "admin" -> navController.navigate("admin_dashboard") { popUpTo(0) }
                            "worker" -> navController.navigate("worker_dashboard") { popUpTo(0) }
                            else -> navController.navigate("home") { popUpTo(0) }
                        }
                    },
                    onNavigateBack = { 
                        viewModel.clearAuthError()
                        navController.popBackStack() 
                    },
                    onNavigateToSignUp = { 
                        viewModel.clearAuthError()
                        navController.navigate("signup") 
                    }
                )
            }
            composable("signup") {
                SignUpScreen(
                    viewModel = viewModel,
                    onSignUpSuccess = { navController.navigate("email_verification") },
                    onNavigateBack = { 
                        viewModel.clearAuthError()
                        navController.popBackStack() 
                    }
                )
            }
            composable("email_verification") {
                VerificationScreen(
                    type = VerificationType.EMAIL,
                    onVerify = { navController.navigate("phone_verification") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("phone_verification") {
                VerificationScreen(
                    type = VerificationType.PHONE,
                    onVerify = { navController.navigate("username_creation") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("username_creation") {
                UsernameScreen(
                    viewModel = viewModel,
                    onUsernameSet = { 
                        val role = viewModel.currentUser.value?.role ?: "customer"
                        if (role == "worker") {
                            navController.navigate("worker_verification")
                        } else {
                            navController.navigate("home")
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onViewCategories = { navController.navigate("categories") },
                    onViewWorkers = { navController.navigate("workers") },
                    onNavigateToProfile = { navController.navigate("profile") }
                )
            }
            composable("profile") {
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTransactions = { navController.navigate("transactions") },
                    onNavigateToSettings = { navController.navigate("settings") },
                    onNavigateToWorkerVerification = { navController.navigate("worker_verification") }
                )
            }
            composable("transactions") {
                TransactionsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("settings") {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate("welcome") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable("calendar") {
                CalendarScreen(viewModel = viewModel)
            }
            composable("worker_verification") {
                WorkerVerificationScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onVerificationComplete = { navController.navigate("home") }
                )
            }
            composable("admin_dashboard") {
                AdminDashboardScreen(
                    viewModel = viewModel,
                    onLogout = {
                        navController.navigate("welcome") { popUpTo(0) { inclusive = true } }
                    }
                )
            }
            composable("payment") {
                PaymentScreen(
                    viewModel = viewModel,
                    onPaymentComplete = { navController.navigate("transactions") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("categories") {
                CategoriesScreen(viewModel = viewModel)
            }
            composable("workers") {
                WorkersScreen(
                    viewModel = viewModel,
                    onNavigateToDashboard = { navController.navigate("worker_dashboard") },
                    onNavigateToWorkerProfile = { navController.navigate("worker_profile") }
                )
            }
            composable("worker_profile") {
                WorkerProfileScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onHireSuccess = { navController.navigate("payment") }
                )
            }
            composable("worker_dashboard") {
                WorkerDashboardScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CustomBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Groups,
                label = "Swarm",
                isSelected = currentRoute == "swarm",
                onClick = { /* TODO */ }
            )
            BottomNavItem(
                icon = Icons.Default.Chat,
                label = "Chat",
                isSelected = currentRoute == "chat",
                onClick = { /* TODO */ }
            )
            
            // Central Home Button
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset(y = (-10).dp)
                    .clip(CircleShape)
                    .background(BeeYellow)
                    .clickable { navController.navigate("home") },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐝", fontSize = 32.sp)
            }

            BottomNavItem(
                icon = Icons.Default.CalendarToday,
                label = "Calendar",
                isSelected = currentRoute == "calendar",
                onClick = { navController.navigate("calendar") }
            )
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                isSelected = currentRoute == "profile",
                onClick = { navController.navigate("profile") }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) BeeYellow else BeeDark.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) BeeYellow else BeeDark.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
    }
}
