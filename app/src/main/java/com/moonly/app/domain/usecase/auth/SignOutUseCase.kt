package com.moonly.app.domain.usecase.auth

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para cerrar sesión
 */
class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<NetworkResult<Unit>> {
        return authRepository.signOut()
    }
}