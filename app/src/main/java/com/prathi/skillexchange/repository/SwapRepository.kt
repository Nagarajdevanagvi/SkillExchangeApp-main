package com.prathi.skillexchange.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.prathi.skillexchange.model.SwapProposal
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SwapRepository {

    private val db =
        FirebaseFirestore.getInstance()

    private val swapsCollection =
        db.collection("swaps")

    // SEND SWAP REQUEST
    suspend fun sendSwapRequest(
        proposal: SwapProposal
    ) {

        val id =
            UUID.randomUUID().toString()

        val newProposal =
            proposal.copy(

                id = id,

                status = "Pending"
            )

        swapsCollection
            .document(id)
            .set(newProposal)
            .await()
    }

    // LISTEN TO ALL USER SWAPS
    // BOTH SENT + RECEIVED
    fun listenToSwaps(
        userId: String
    ): Flow<List<SwapProposal>> = callbackFlow {

        val listener = swapsCollection

            .addSnapshotListener {

                    snapshot,
                    error ->

                if (error != null) {

                    close(error)

                    return@addSnapshotListener
                }

                val allSwaps =

                    snapshot
                        ?.toObjects(
                            SwapProposal::class.java
                        )

                        ?: emptyList()

                // FILTER USER RELATED SWAPS
                val userSwaps =

                    allSwaps.filter {

                        it.senderId == userId ||

                                it.receiverId == userId
                    }

                        .sortedByDescending {

                            it.id
                        }

                trySend(userSwaps)
            }

        awaitClose {

            listener.remove()
        }
    }

    // ACCEPT SWAP
    suspend fun acceptSwap(
        swapId: String
    ) {

        swapsCollection
            .document(swapId)
            .update(
                "status",
                "Accepted"
            )
            .await()
    }

    // REJECT SWAP
    suspend fun rejectSwap(
        swapId: String
    ) {

        swapsCollection
            .document(swapId)
            .update(
                "status",
                "Rejected"
            )
            .await()
    }

    // START SWAP
    suspend fun startSwap(
        swapId: String
    ) {

        swapsCollection
            .document(swapId)
            .update(
                "status",
                "Ongoing"
            )
            .await()
    }

    // COMPLETE SWAP
    suspend fun completeSwap(
        swapId: String
    ) {

        swapsCollection
            .document(swapId)
            .update(
                "status",
                "Completed"
            )
            .await()
    }

    // CANCEL SWAP
    suspend fun cancelSwap(
        swapId: String
    ) {

        swapsCollection
            .document(swapId)
            .update(
                "status",
                "Cancelled"
            )
            .await()
    }
}