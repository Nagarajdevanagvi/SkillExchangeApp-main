package com.prathi.skillexchange.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.prathi.skillexchange.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val db =
        FirebaseFirestore.getInstance()

    private val usersCollection =
        db.collection("users")

    suspend fun saveUser(
        user: User
    ): Boolean {

        return try {

            if (user.id.isNotEmpty()) {

                usersCollection
                    .document(user.id)
                    .set(user)
                    .await()

                true

            } else {

                false
            }

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }

    suspend fun getCurrentUser(
        userId: String
    ): User? {

        return try {

            val snapshot =

                usersCollection
                    .document(userId)
                    .get()
                    .await()

            snapshot.toObject(User::class.java)

        } catch (e: Exception) {

            e.printStackTrace()

            null
        }
    }

    // FIXED USER FETCH
    suspend fun getAllUsers(): List<User> {

        return try {

            val snapshot =

                usersCollection
                    .get()
                    .await()

            snapshot.documents.mapNotNull { document ->

                val user =

                    document.toObject(
                        User::class.java
                    )

                user?.copy(
                    id = document.id
                )
            }

        } catch (e: Exception) {

            e.printStackTrace()

            emptyList()
        }
    }

    suspend fun updateOnlineStatus(

        userId: String,

        isOnline: Boolean
    ) {

        try {

            usersCollection
                .document(userId)
                .update(
                    "isOnline",
                    isOnline
                )
                .await()

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}