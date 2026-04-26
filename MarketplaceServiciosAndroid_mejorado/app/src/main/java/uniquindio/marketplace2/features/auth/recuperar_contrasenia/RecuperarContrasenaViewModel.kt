package uniquindio.marketplace2.features.auth.recuperar_contrasenia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecuperarContrasenaViewModel @Inject constructor() : ViewModel() {

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo.asStateFlow()

    private val _errorCorreo = MutableStateFlow<String?>(null)
    val errorCorreo: StateFlow<String?> = _errorCorreo.asStateFlow()

    private val _mensajeResultado = MutableStateFlow<String?>(null)
    val mensajeResultado: StateFlow<String?> = _mensajeResultado.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun alCambiarCorreo(nuevoCorreo: String) {
        _correo.value = nuevoCorreo
        if (_errorCorreo.value != null) {
            _errorCorreo.value = null
        }
    }

    fun recuperarContrasena() {
        if (_correo.value.isBlank()) {
            _errorCorreo.value = "El correo es requerido"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_correo.value).matches()) {
            _errorCorreo.value = "Correo no válido"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            delay(1500)
            _mensajeResultado.value = "Se ha enviado un enlace para restablecer tu contraseña"
            _isLoading.value = false
        }
    }

    fun limpiarMensaje() {
        _mensajeResultado.value = null
    }
}