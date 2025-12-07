package com.moonly.app.data.repository

import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.api.ProfileApi
import com.moonly.app.data.remote.dto.common.ErrorResponse
import com.moonly.app.data.remote.dto.profile.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi,
    private val preferencesDataStore: PreferencesDataStore,
    private val gson: Gson
) {

    /**
     * Obtiene el perfil del usuario
     */
    fun getProfile(): Flow<NetworkResult<ProfileResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = profileApi.getProfile()

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener perfil"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Actualiza el perfil del usuario
     */
    fun updateProfile(nickname: String?, birthYear: Int?): Flow<NetworkResult<ProfileResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = ProfileUpdateRequest(nickname, birthYear)
            val response = profileApi.updateProfile(request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al actualizar perfil"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Configuración de primer ingreso
     */
    fun firstLoginSetup(nickname: String?, birthYear: Int?): Flow<NetworkResult<ProfileResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = FirstLoginSetupRequest(nickname, birthYear)
            val response = profileApi.firstLoginSetup(request)

            if (response.isSuccessful && response.body() != null) {
                // Marcar onboarding como completado
                preferencesDataStore.saveHasCompletedOnboarding(true)
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error en configuración inicial"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Elimina la cuenta del usuario
     */
    fun deleteAccount(): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = profileApi.deleteAccount()

            if (response.isSuccessful && response.body() != null) {
                // Limpiar todos los datos locales
                preferencesDataStore.clearAll()
                emit(NetworkResult.Success(response.body()!!.message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al eliminar cuenta"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Obtiene la configuración del usuario
     */
    fun getSettings(): Flow<NetworkResult<UserSettingsResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = profileApi.getSettings()

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al obtener configuración"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Actualiza la configuración del usuario
     */
    fun updateSettings(request: UserSettingsUpdateRequest): Flow<NetworkResult<UserSettingsResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = profileApi.updateSettings(request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al actualizar configuración"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Actualiza el FCM token
     */
    fun updateFcmToken(fcmToken: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = FcmTokenRequest(fcmToken)
            val response = profileApi.updateFcmToken(request)

            if (response.isSuccessful && response.body() != null) {
                // Guardar token localmente
                preferencesDataStore.saveFcmToken(fcmToken)
                emit(NetworkResult.Success(response.body()!!.message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al actualizar FCM token"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }
}