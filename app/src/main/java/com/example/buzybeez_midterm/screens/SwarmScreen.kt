package com.example.buzybeez_midterm.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buzybeez_midterm.AppViewModel
import com.example.buzybeez_midterm.ui.theme.BeeDark
import com.example.buzybeez_midterm.ui.theme.BeeGray
import com.example.buzybeez_midterm.ui.theme.BeeYellow

data class SwarmPost(
    val id: String,
    val authorName: String,
    val timeAgo: String,
    val content: String,
    var likes: Int,
    var comments: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwarmScreen(
    viewModel: AppViewModel
) {
    val posts = remember {
        mutableStateListOf(
            SwarmPost("1", "Maria Santos", "2h ago", "Looking for recommendations for reliable plumbing services around Bacolod City. Any suggestions?", 12, 4),
            SwarmPost("2", "Juan Dela Cruz", "5h ago", "Just finished helping out a wonderful family with home cleaning! Buzy Beez is amazing 🐝", 25, 8),
            SwarmPost("3", "Ana Reyes", "1d ago", "Offering discounted electrical troubleshooting this weekend. DM to book!", 19, 3)
        )
    }

    var showCreateDialog by remember { mutableStateOf(false) }
    var newPostContent by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("The Swarm 🐝", fontWeight = FontWeight.Bold, color = BeeDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFDFBF5))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = BeeYellow,
                contentColor = BeeDark
            ) {
                Icon(Icons.Default.Add, "Create Post")
            }
        },
        containerColor = Color(0xFFFDFBF5)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Community Feed & Neighborhood Updates",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BeeGray
                )
            }
            items(posts) { post ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(BeeYellow),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(post.authorName.take(1), fontWeight = FontWeight.Bold, color = BeeDark)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(post.authorName, fontWeight = FontWeight.Bold, color = BeeDark)
                                Text(post.timeAgo, fontSize = 10.sp, color = BeeGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(post.content, color = BeeDark, fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { post.likes++ }
                            ) {
                                Icon(Icons.Default.Favorite, null, tint = Color.Red, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${post.likes}", color = BeeGray, fontSize = 12.sp)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ModeComment, null, tint = BeeGray, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${post.comments}", color = BeeGray, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("Create Swarm Post", color = BeeDark) },
                text = {
                    OutlinedTextField(
                        value = newPostContent,
                        onValueChange = { newPostContent = it },
                        placeholder = { Text("What's happening in your hive?") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newPostContent.isNotBlank()) {
                                posts.add(0, SwarmPost(
                                    id = System.currentTimeMillis().toString(),
                                    authorName = viewModel.currentUser.value?.fullName ?: "Community Member",
                                    timeAgo = "Just now",
                                    content = newPostContent,
                                    likes = 0,
                                    comments = 0
                                ))
                                newPostContent = ""
                                showCreateDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BeeYellow)
                    ) {
                        Text("Post", color = BeeDark)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel", color = BeeGray)
                    }
                }
            )
        }
    }
}
