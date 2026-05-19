package com.prathi.skillexchange.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import com.google.firebase.FirebaseException

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    private val firestore = FirebaseFirestore.getInstance()

    private var verificationId: String = ""

    // EMAIL LOGIN
    suspend fun login(
        email: String,
        password: String
    ): Result<String> {

        return try {

            auth.signInWithEmailAndPassword(
                email,
                password
            ).await()

            Result.success("Login Successful")

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // EMAIL REGISTER
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<String> {

        return try {

            val result =
                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).await()

            val userId =
                result.user?.uid ?: ""

            val userMap = hashMapOf(

                "uid" to userId,

                "name" to name,

                "email" to email,

                "phone" to "",

                "bio" to "",

                "location" to "",

                "profileImage" to "",

                "skillsOffered" to emptyList<String>(),

                "skillsWanted" to emptyList<String>(),

                "isOnline" to true
            )

            firestore.collection("users")
                .document(userId)
                .set(userMap)
                .await()

            Result.success(
                "Registration Successful"
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // SEND OTP
    fun sendOtp(

        phone: String,

        activity: Activity,

        onCodeSent: () -> Unit,

        onError: (String) -> Unit
    ) {

        val options =

            PhoneAuthOptions.newBuilder(auth)

                .setPhoneNumber(phone)

                .setTimeout(
                    60L,
                    TimeUnit.SECONDS
                )

                .setActivity(activity)

                .setCallbacks(

                    object :
                        PhoneAuthProvider
                        .OnVerificationStateChangedCallbacks() {

                        override fun onVerificationCompleted(
                            credential: PhoneAuthCredential
                        ) {

                            auth.signInWithCredential(
                                credential
                            )
                        }

                        override fun onVerificationFailed(
                            e: FirebaseException
                        ) {

                            onError(
                                e.message
                                    ?: "OTP Failed"
                            )
                        }

                        override fun onCodeSent(

                            verificationId: String,

                            token:
                            PhoneAuthProvider
                            .ForceResendingToken
                        ) {

                            this@AuthRepository
                                .verificationId =
                                verificationId

                            onCodeSent()
                        }
                    }
                )
                .build()

        PhoneAuthProvider
            .verifyPhoneNumber(options)
    }

    // VERIFY OTP
    suspend fun verifyOtp(
        otp: String
    ): Result<String> {

        return try {

            val credential =

                PhoneAuthProvider.getCredential(
                    verificationId,
                    otp
                )

            val result =

                auth.signInWithCredential(
                    credential
                ).await()

            val firebaseUser =
                result.user

            val uid =
                firebaseUser?.uid ?: ""

            val phone =
                firebaseUser?.phoneNumber ?: ""

            val userDoc =

                firestore.collection("users")
                    .document(uid)
                    .get()
                    .await()

            // CREATE USER ONLY IF NOT EXISTS
            if (!userDoc.exists()) {

                val userMap = hashMapOf(

                    "id" to uid,

                    "name" to "",

                    "email" to "",

                    "phone" to phone,

                    "bio" to "",

                    "location" to "",

                    "profileImage" to "",

                    "skillsOffered" to emptyList<String>(),

                    "skillsWanted" to emptyList<String>(),

                    "isOnline" to true
                )

                firestore.collection("users")
                    .document(uid)
                    .set(userMap)
                    .await()
            }

            Result.success(
                "OTP Login Successful"
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    // LOGOUT
    fun logout() {

        FirebaseAuth
            .getInstance()
            .signOut()
    }

    // CURRENT USER ID
    fun getCurrentUserId(): String? {

        return auth.currentUser?.uid
    }

    // AUTO LOGIN CHECK
    fun isUserLoggedIn(): Boolean {

        return auth.currentUser != null
    }
}