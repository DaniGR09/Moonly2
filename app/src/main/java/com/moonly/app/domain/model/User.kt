package com.moonly.app.domain.model

import com.moonly.app.data.remote.dto.profile.ProfileResponse

/**
 * Modelo de dominio para Usuario
 */
data class User(
    val id: String,
    val email: String,
    val nickname: String?,
    val birthYear: Int?,
    val emailVerifiedAt: String?,
    val createdAt: String,
    val updatedAt: String
)

/**
 * Mapper: DTO -> Domain
 */
fun ProfileResponse.toUser(): User {
    return User(
        id = this.id,
        email = this.email,
        nickname = this.nickname,
        birthYear = this.birthYear,
        emailVerifiedAt = this.emailVerifiedAt,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}