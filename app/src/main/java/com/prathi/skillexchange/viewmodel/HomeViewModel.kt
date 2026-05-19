package com.prathi.skillexchange.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.prathi.skillexchange.model.SwapProposal
import com.prathi.skillexchange.model.User
import com.prathi.skillexchange.repository.AuthRepository
import com.prathi.skillexchange.repository.SwapRepository
import com.prathi.skillexchange.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {

    private val userRepository =
        UserRepository()

    private val swapRepository =
        SwapRepository()

    private val authRepository =
        AuthRepository()

    private val db =
        FirebaseFirestore.getInstance()

    private val _users =
        MutableStateFlow<List<User>>(
            emptyList()
        )

    val users:
            StateFlow<List<User>>
            = _users

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading:
            StateFlow<Boolean>
            = _isLoading

    init {

        loadUsers()
    }

    fun loadUsers() {

        viewModelScope.launch {

            _isLoading.value = true

            try {

                val allUsers =
                    userRepository.getAllUsers()

                val currentUserId =
                    authRepository.getCurrentUserId()

                _users.value =
                    allUsers.filter {

                        it.id != currentUserId
                    }

            } catch (e: Exception) {

                e.printStackTrace()

            } finally {

                _isLoading.value = false
            }
        }
    }

    // REAL SWAP REQUEST
    fun requestSwap(
        receiver: User
    ) {

        val senderId =
            authRepository.getCurrentUserId()
                ?: return

        viewModelScope.launch {

            try {

                // FETCH CURRENT USER DIRECTLY FROM FIRESTORE
                val senderSnapshot =

                    db.collection("users")
                        .document(senderId)
                        .get()
                        .await()

                val sender =
                    senderSnapshot.toObject(
                        User::class.java
                    )

                val proposal = SwapProposal(

                    senderId = senderId,

                    receiverId = receiver.id,

                    senderName =
                        sender?.name ?: "Unknown",

                    offeredSkill =

                        sender?.skillsOffered
                            ?.firstOrNull()
                            ?: "General Skill",

                    wantedSkill =

                        receiver.skillsWanted
                            .firstOrNull()
                            ?: "General Skill"
                )

                swapRepository
                    .sendSwapRequest(proposal)

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun refreshUsers() {

        loadUsers()
    }
}