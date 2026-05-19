package com.prathi.skillexchange.model


data class User(


    val id: String = "",


    val name: String = "",


    val email: String = "",


    val phone: String = "",


    val bio: String = "",


    val location: String = "",


    val profileImage: String = "",


    val skillsOffered: List<String> = emptyList(),


    val skillsWanted: List<String> = emptyList(),


    val isOnline: Boolean = false
)


