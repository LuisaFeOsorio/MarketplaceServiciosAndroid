package uniquindio.marketplace2.features.auth.registro

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
class RegistroViewModel @Inject constructor(
    private val repositorioAuth: RepositorioAuth,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _rol = MutableStateFlow("cliente")
    val rol: StateFlow<String> = _rol.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _registroExitoso = MutableStateFlow<Usuario?>(null)
    val registroExitoso: StateFlow<Usuario?> = _registroExitoso.asStateFlow()

    fun onNombreChange(v: String) { _nombre.value = v }
    fun onEmailChange(v: String) { _email.value = v }
    fun onPasswordChange(v: String) { _password.value = v }
    fun onRolChange(v: String) { _rol.value = v }

    fun registrar() {
        if (_nombre.value.isBlank() || _email.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Todos los campos son obligatorios"
            return
        }
        if (_password.value.length < 6) {
            _error.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            delay(800)
            val nuevo = Usuario(
                id = "", nombre = _nombre.value.trim(),
                email = _email.value.trim(), rol = _rol.value
            )
            val creado = repositorioAuth.registrar(nuevo)
            if (creado != null) {
                sessionManager.saveSession(creado.id, creado.nombre, creado.email, creado.rol)
                _registroExitoso.value = creado
            } else {
                _error.value = "El correo ya está registrado"
            }
            _isLoading.value = false
        }
    }

    fun resetRegistroExitoso() { _registroExitoso.value = null }
}
