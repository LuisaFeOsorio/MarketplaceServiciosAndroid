package uniquindio.marketplace2.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.core.datastore.SessionManager
import uniquindio.marketplace2.data.modelos.Usuario
import uniquindio.marketplace2.data.repositorios.RepositorioAuth
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repositorioAuth: RepositorioAuth,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _loginSuccess = MutableStateFlow<Usuario?>(null)
    val loginSuccess: StateFlow<Usuario?> = _loginSuccess.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    private val _rememberMe = MutableStateFlow(false)
    val rememberMe: StateFlow<Boolean> = _rememberMe.asStateFlow()

    fun onEmailChange(value: String) { _email.value = value; _errorMessage.value = null }
    fun onPasswordChange(value: String) { _password.value = value; _errorMessage.value = null }
    fun togglePasswordVisibility() { _passwordVisible.value = !_passwordVisible.value }
    fun onRememberMeChange(value: Boolean) { _rememberMe.value = value }

    fun iniciarSesion() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _errorMessage.value = "Completa todos los campos"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            delay(800)
            val usuario = repositorioAuth.login(_email.value.trim(), _password.value.trim())
            if (usuario != null) {
                sessionManager.saveSession(
                    userId = usuario.id,
                    nombre = usuario.nombre,
                    email = usuario.email,
                    rol = usuario.rol
                )
                _loginSuccess.value = usuario
            } else {
                _errorMessage.value = "Email o contraseña incorrectos"
            }
            _isLoading.value = false
        }
    }

    fun loginPrueba(rol: String) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(400)
            val usuario = when (rol) {
                "cliente"    -> repositorioAuth.login("cliente@test.com", "123456")
                "trabajador" -> repositorioAuth.login("trabajador@test.com", "123456")
                "moderador"  -> repositorioAuth.login("moderador@test.com", "123456")
                else         -> null
            }
            if (usuario != null) {
                sessionManager.saveSession(usuario.id, usuario.nombre, usuario.email, usuario.rol)
                _loginSuccess.value = usuario
            }
            _isLoading.value = false
        }
    }

    fun resetLoginSuccess() { _loginSuccess.value = null }
}
