package uniquindio.marketplace2.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NOMBRE = stringPreferencesKey("user_nombre")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_ROL = stringPreferencesKey("user_rol")
        private val IS_LOGGED_IN = stringPreferencesKey("is_logged_in")
    }

    suspend fun saveSession(
        userId: String,
        nombre: String,
        email: String,
        rol: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_NOMBRE] = nombre
            preferences[USER_EMAIL] = email
            preferences[USER_ROL] = rol
            preferences[IS_LOGGED_IN] = "true"
        }
    }

    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }
    }

    fun getUserNombre(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_NOMBRE]
        }
    }

    fun getUserEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }
    }

    fun getUserRol(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ROL]
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] == "true"
        }
    }

    fun getUserInfo(): Flow<UserSession?> {
        return context.dataStore.data.map { preferences ->
            val isLoggedIn = preferences[IS_LOGGED_IN] == "true"
            if (isLoggedIn) {
                UserSession(
                    userId = preferences[USER_ID] ?: "",
                    nombre = preferences[USER_NOMBRE] ?: "",
                    email = preferences[USER_EMAIL] ?: "",
                    rol = preferences[USER_ROL] ?: ""
                )
            } else {
                null
            }
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

data class UserSession(
    val userId: String,
    val nombre: String,
    val email: String,
    val rol: String
)