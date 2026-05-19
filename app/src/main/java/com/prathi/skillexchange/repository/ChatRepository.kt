package com.prathi.skillexchange.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.prathi.skillexchange.model.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ChatRepository {

    private val db =
        FirebaseFirestore.getInstance()

    private val messagesCollection =
        db.collection("messages")

    // SEND MESSAGE
    suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        text: String
    ) {

        val id =
            UUID.randomUUID().toString()

        val message = Message(

            id = id,

            senderId = senderId,

            receiverId = receiverId,

            text = text,

            timestamp =
                System.currentTimeMillis()
        )

        messagesCollection
            .document(id)
            .set(message)
            .await()
    }

    // REAL-TIME MESSAGE LISTENER
    fun listenToMessages(
        currentUserId: String,
        otherUserId: String
    ): Flow<List<Message>> = callbackFlow {

        val listener = messagesCollection

            .orderBy(
                "timestamp",
                Query.Direction.ASCENDING
            )

            .addSnapshotListener {

                    snapshot,
                    error ->

                if (error != null) {

                    close(error)

                    return@addSnapshotListener
                }

                val allMessages =

                    snapshot
                        ?.toObjects(Message::class.java)
                        ?: emptyList()

                val filteredMessages =

                    allMessages.filter {

                        (
                                it.senderId == currentUserId &&
                                        it.receiverId == otherUserId
                                )

                                ||

                                (
                                        it.senderId == otherUserId &&
                                                it.receiverId == currentUserId
                                        )
                    }

                trySend(filteredMessages)
            }

        awaitClose {

            listener.remove()
        }
    }
}