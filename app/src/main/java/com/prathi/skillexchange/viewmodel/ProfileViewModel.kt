package com.prathi.skillexchange.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.prathi.skillexchange.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

    private val _user =
        MutableStateFlow<User?>(null)

    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        loadUser()
    }

    fun loadUser() {

        viewModelScope.launch {

            try {

                val uid =
                    auth.currentUser?.uid ?: return@launch
                println("CURRENT UID = $uid")

                val document =
                    firestore.collection("users")
                        .document(uid)
                        .get()
                        .await()

                val userData =
                    document.toObject(User::class.java)

                _user.value = userData

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
    fun refreshUser() {

        _user.value = null

        loadUser()
    }

    fun saveProfile(
        name: String,
        phone: String,
        bio: String,
        location: String,
        skillsOffered: List<String>,
        skillsWanted: List<String>
    ) {

        viewModelScope.launch {
            _isLoading.value = true
            try {

                val uid =
                    auth.currentUser?.uid ?: return@launch

                val updatedUser = User(

                    id = uid,

                    name = name,

                    email = _user.value?.email ?: "",

                    phone = phone,

                    bio = bio,

                    location = location,

                    profileImage =
                        _user.value?.profileImage ?: "",

                    skillsOffered = skillsOffered,

                    skillsWanted = skillsWanted,

                    isOnline = true
                )

                firestore.collection("users")
                    .document(uid)
                    .set(updatedUser)
                    .await()

                _user.value = updatedUser
                _message.value = "Profile updated successfully!"

            } catch (e: Exception) {

                _message.value = "Failed to update profile"
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }
}