package com.prathi.skillexchange.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathi.skillexchange.model.Message
import com.prathi.skillexchange.repository.AuthRepository
import com.prathi.skillexchange.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val chatRepository =
        ChatRepository()

    private val authRepository =
        AuthRepository()

    private val _messages =
        MutableStateFlow<List<Message>>(
            emptyList()
        )

    val messages:
            StateFlow<List<Message>>
            = _messages

    // LOAD REAL-TIME CHAT
    fun loadMessages(
        otherUserId: String
    ) {

        val currentUserId =
            authRepository.getCurrentUserId()
                ?: return

        viewModelScope.launch {

            chatRepository
                .listenToMessages(
                    currentUserId,
                    otherUserId
                )

                .collect {

                    _messages.value = it
                }
        }
    }

    // SEND MESSAGE
    fun sendMessage(
        receiverId: String,
        text: String
    ) {

        val senderId =
            authRepository.getCurrentUserId()
                ?: return

        viewModelScope.launch {

            try {

                chatRepository.sendMessage(

                    senderId = senderId,

                    receiverId = receiverId,

                    text = text
                )

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun getCurrentUserId(): String {

        return authRepository
            .getCurrentUserId()
            ?: ""
    }
}