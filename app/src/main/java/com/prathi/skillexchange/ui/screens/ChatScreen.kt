package com.prathi.skillexchange.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prathi.skillexchange.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(

    otherUserId: String,

    userName: String,

    onNavigateBack: () -> Unit,

    onNavigateHome: () -> Unit,

    onNavigateSwap: () -> Unit,

    onNavigateProfile: () -> Unit
) {

    val chatViewModel: ChatViewModel =
        viewModel()

    val messages =
        chatViewModel.messages.collectAsState()

    var messageText by remember {
        mutableStateOf("")
    }

    val currentUserId =
        chatViewModel.getCurrentUserId()

    LaunchedEffect(Unit) {

        if (otherUserId.isNotEmpty()) {

            chatViewModel.loadMessages(
                otherUserId
            )
        }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box {

                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                    ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(

                                    text =
                                        userName.take(1),

                                    color = Color.White,

                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color.Green)
                                    .align(
                                        Alignment.BottomEnd
                                    )
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.width(12.dp)
                        )

                        Column {

                            Text(

                                text = userName,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Text(

                                text = "Online",

                                fontSize = 12.sp,

                                color = Color.Gray
                            )
                        }
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onNavigateBack
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,

                            contentDescription =
                                "Back"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F7FB))
        ) {

            LazyColumn(

                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp),

                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                items(messages.value) { message ->

                    val isMine =
                        message.senderId ==
                                currentUserId

                    Column(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalAlignment =

                            if (isMine)
                                Alignment.End
                            else
                                Alignment.Start
                    ) {

                        Card(

                            colors =
                                CardDefaults.cardColors(

                                    containerColor =

                                        if (isMine)
                                            MaterialTheme
                                                .colorScheme
                                                .primary

                                        else
                                            Color.White
                                ),

                            shape =
                                RoundedCornerShape(18.dp)
                        ) {

                            Column(

                                modifier =
                                    Modifier.padding(12.dp)
                            ) {

                                Text(

                                    text = message.text,

                                    color =

                                        if (isMine)
                                            Color.White

                                        else
                                            Color.Black
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(4.dp)
                                )

                                Text(

                                    text =

                                        SimpleDateFormat(
                                            "hh:mm a",
                                            Locale.getDefault()
                                        ).format(
                                            Date(
                                                message.timestamp
                                            )
                                        ),

                                    fontSize = 11.sp,

                                    color =

                                        if (isMine)

                                            Color.White.copy(
                                                alpha = 0.7f
                                            )

                                        else
                                            Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            Surface(

                tonalElevation = 3.dp,

                shadowElevation = 8.dp
            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    OutlinedTextField(

                        value = messageText,

                        onValueChange = {

                            messageText = it
                        },

                        placeholder = {

                            Text("Type a message...")
                        },

                        modifier =
                            Modifier.weight(1f),

                        shape =
                            RoundedCornerShape(24.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )

                    FloatingActionButton(

                        onClick = {

                            if (
                                messageText.isNotBlank()
                            ) {

                                chatViewModel.sendMessage(

                                    receiverId =
                                        otherUserId,

                                    text =
                                        messageText
                                )

                                messageText = ""
                            }
                        },

                        containerColor =
                            MaterialTheme
                                .colorScheme
                                .primary
                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.Send,

                            contentDescription =
                                "Send"
                        )
                    }
                }
            }
        }
    }
}