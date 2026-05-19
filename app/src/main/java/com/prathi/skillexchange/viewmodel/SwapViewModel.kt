package com.prathi.skillexchange.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prathi.skillexchange.model.SwapProposal
import com.prathi.skillexchange.repository.AuthRepository
import com.prathi.skillexchange.repository.SwapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SwapViewModel : ViewModel() {

    private val swapRepository =
        SwapRepository()

    private val authRepository =
        AuthRepository()

    private val _swaps =
        MutableStateFlow<List<SwapProposal>>(
            emptyList()
        )

    val swaps:
            StateFlow<List<SwapProposal>>
            = _swaps

    private val _message =
        MutableStateFlow<String?>(null)

    val message:
            StateFlow<String?>
            = _message

    init {

        loadSwaps()
    }

    private fun loadSwaps() {

        val userId =
            authRepository.getCurrentUserId()

        if (userId != null) {

            viewModelScope.launch {

                swapRepository
                    .listenToSwaps(userId)
                    .collect {

                        _swaps.value = it
                    }
            }
        }
    }

    // SEND SWAP REQUEST
    fun sendSwapRequest(

        receiverId: String,

        senderName: String,

        offeredSkill: String,

        wantedSkill: String
    ) {

        val senderId =
            authRepository.getCurrentUserId()
                ?: return

        val proposal = SwapProposal(

            senderId = senderId,

            receiverId = receiverId,

            senderName = senderName,

            offeredSkill = offeredSkill,

            wantedSkill = wantedSkill,

            status = "Pending"
        )

        viewModelScope.launch {

            try {

                swapRepository
                    .sendSwapRequest(proposal)

                _message.value =
                    "Swap Request Sent!"

            } catch (e: Exception) {

                _message.value =
                    "Failed to send request"
            }
        }
    }

    // ACCEPT SWAP
    fun acceptSwap(
        swapId: String
    ) {

        viewModelScope.launch {

            try {

                swapRepository
                    .acceptSwap(swapId)

                _message.value =
                    "Swap Accepted!"

            } catch (e: Exception) {

                _message.value =
                    "Failed to accept swap"
            }
        }
    }

    // REJECT SWAP
    fun rejectSwap(
        swapId: String
    ) {

        viewModelScope.launch {

            try {

                swapRepository
                    .rejectSwap(swapId)

                _message.value =
                    "Swap Rejected!"

            } catch (e: Exception) {

                _message.value =
                    "Failed to reject swap"
            }
        }
    }

    // START SWAP
    fun startSwap(
        swapId: String
    ) {

        viewModelScope.launch {

            try {

                swapRepository
                    .startSwap(swapId)

                _message.value =
                    "Swap Started!"

            } catch (e: Exception) {

                _message.value =
                    "Failed to start swap"
            }
        }
    }

    // COMPLETE SWAP
    fun completeSwap(
        swapId: String
    ) {

        viewModelScope.launch {

            try {

                swapRepository
                    .completeSwap(swapId)

                _message.value =
                    "Swap Completed!"

            } catch (e: Exception) {

                _message.value =
                    "Failed to complete swap"
            }
        }
    }

    // CANCEL SWAP
    fun cancelSwap(
        swapId: String
    ) {

        viewModelScope.launch {

            try {

                swapRepository
                    .cancelSwap(swapId)

                _message.value =
                    "Swap Cancelled!"

            } catch (e: Exception) {

                _message.value =
                    "Failed to cancel swap"
            }
        }
    }
}