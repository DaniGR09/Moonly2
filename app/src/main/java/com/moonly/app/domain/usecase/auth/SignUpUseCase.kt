package com.moonly.app.domain.usecase.auth

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.dto.auth.AuthResponse
import com.moonly.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para registrar usuario
 */
class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<NetworkResult<AuthResponse>> {
        return authRepository.signUp(email, password)
    }
}