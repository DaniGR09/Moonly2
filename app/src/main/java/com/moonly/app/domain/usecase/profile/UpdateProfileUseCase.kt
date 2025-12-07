package com.moonly.app.domain.usecase.profile

import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.repository.ProfileRepository
import com.moonly.app.domain.model.User
import com.moonly.app.domain.model.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Caso de uso para actualizar perfil
 */
class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(nickname: String?, birthYear: Int?): Flow<NetworkResult<User>> {
        return profileRepository.updateProfile(nickname, birthYear).map { result ->
            when (result) {
                is NetworkResult.Success -> NetworkResult.Success(result.data.toUser())
                is NetworkResult.Error -> NetworkResult.Error(result.message, result.code)
                is NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }
}