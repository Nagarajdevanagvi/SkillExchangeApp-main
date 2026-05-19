package com.prathi.skillexchange.model

data class SwapProposal(

    val id: String = "",

    val senderId: String = "",

    val receiverId: String = "",

    val senderName: String = "",

    val offeredSkill: String = "",

    val wantedSkill: String = "",

    val status: String = "Pending",

    val timestamp: Long =
        System.currentTimeMillis()
)