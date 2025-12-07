package com.moonly.app.data.repository

import com.moonly.app.data.local.datastore.PreferencesDataStore
import com.moonly.app.data.remote.NetworkResult
import com.moonly.app.data.remote.api.AuthApi
import com.moonly.app.data.remote.dto.auth.*
import com.moonly.app.data.remote.dto.common.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesDataStore: PreferencesDataStore,
    private val gson: Gson
) {

    /**
     * Registro de usuario
     */
    fun signUp(email: String, password: String): Flow<NetworkResult<AuthResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = SignUpRequest(email, password)
            val response = authApi.signUp(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Guardar tokens y datos del usuario
                authResponse.session.accessToken?.let {
                    preferencesDataStore.saveAccessToken(it)
                }
                authResponse.session.refreshToken?.let {
                    preferencesDataStore.saveRefreshToken(it)
                }
                preferencesDataStore.saveUserId(authResponse.user.id)
                preferencesDataStore.saveUserEmail(authResponse.user.email)
                preferencesDataStore.saveIsLoggedIn(true)
                preferencesDataStore.saveIsEmailVerified(authResponse.user.emailConfirmedAt != null)

                // ✅ CRÍTICO: Marcar que NO ha completado onboarding
                preferencesDataStore.saveHasCompletedOnboarding(false)

                emit(NetworkResult.Success(authResponse))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al registrar usuario"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Inicio de sesión
     */
    fun signIn(email: String, password: String): Flow<NetworkResult<AuthResponse>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = SignInRequest(email, password)
            val response = authApi.signIn(request)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Guardar tokens y datos del usuario
                preferencesDataStore.saveAccessToken(authResponse.session.accessToken!!)
                preferencesDataStore.saveRefreshToken(authResponse.session.refreshToken!!)
                preferencesDataStore.saveUserId(authResponse.user.id)
                preferencesDataStore.saveUserEmail(authResponse.user.email)
                preferencesDataStore.saveIsLoggedIn(true)
                preferencesDataStore.saveIsEmailVerified(authResponse.user.emailConfirmedAt != null)

                emit(NetworkResult.Success(authResponse))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Credenciales inválidas"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Cierre de sesión
     */
    fun signOut(): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading)

        try {
            val response = authApi.signOut()

            // Limpiar datos locales independientemente de la respuesta
            preferencesDataStore.clearSession()

            if (response.isSuccessful) {
                emit(NetworkResult.Success(Unit))
            } else {
                // Aunque falle la API, si limpiamos local consideramos éxito
                emit(NetworkResult.Success(Unit))
            }
        } catch (e: Exception) {
            // Aunque falle, limpiamos local
            preferencesDataStore.clearSession()
            emit(NetworkResult.Success(Unit))
        }
    }

    /**
     * Recuperación de contraseña
     */
    fun resetPassword(email: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = PasswordResetRequest(email)
            val response = authApi.resetPassword(request)

            if (response.isSuccessful && response.body() != null) {
                val message = response.body()!!.message
                emit(NetworkResult.Success(message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al enviar correo de recuperación"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Cambio de contraseña (desde perfil)
     */
    fun changePassword(currentPassword: String, newPassword: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = PasswordChangeRequest(currentPassword, newPassword)
            val response = authApi.changePassword(request)

            if (response.isSuccessful && response.body() != null) {
                val message = response.body()!!.message
                emit(NetworkResult.Success(message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al cambiar contraseña"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * ⚠️ ARREGLADO: Actualiza contraseña desde deep link (con Flow)
     * Este método se usa cuando el usuario viene del link de recuperación
     */
    fun updatePassword(newPassword: String): Flow<NetworkResult<String>> = flow {
        emit(NetworkResult.Loading)

        try {
            val request = UpdatePasswordRequest(newPassword)
            val response = authApi.updatePassword(request)

            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!.message))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    gson.fromJson(errorBody, ErrorResponse::class.java).detail
                } catch (e: Exception) {
                    "Error al actualizar contraseña"
                }
                emit(NetworkResult.Error(errorMessage, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Error desconocido"))
        }
    }

    /**
     * Verifica si el usuario está logueado
     */
    fun isLoggedIn(): Flow<Boolean> {
        return preferencesDataStore.getIsLoggedIn()
    }

    /**
     * Verifica si el email está verificado
     */
    fun isEmailVerified(): Flow<Boolean> {
        return preferencesDataStore.getIsEmailVerified()
    }
}