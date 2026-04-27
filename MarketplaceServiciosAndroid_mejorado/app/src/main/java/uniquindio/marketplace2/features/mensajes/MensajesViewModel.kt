package uniquindio.marketplace2.features.mensajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uniquindio.marketplace2.core.datastore.SessionManager
import uniquindio.marketplace2.data.modelos.Mensaje
import uniquindio.marketplace2.data.repositorios.RepositorioMensajes
import javax.inject.Inject

@HiltViewModel
class MensajesViewModel @Inject constructor(
    private val repositorioMensajes: RepositorioMensajes,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _mensajes = MutableStateFlow<List<Mensaje>>(emptyList())
    val mensajes: StateFlow<List<Mensaje>> = _mensajes.asStateFlow()

    private val _textoActual = MutableStateFlow("")
    val textoActual: StateFlow<String> = _textoActual.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _usuarioId = MutableStateFlow("")
    val usuarioId: StateFlow<String> = _usuarioId.asStateFlow()

    private val _usuarioNombre = MutableStateFlow("")
    private val _usuarioRol = MutableStateFlow("")

    fun cargarMensajes(solicitudId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val sesion = sessionManager.getUserInfo().first()
            if (sesion != null) {
                _usuarioId.value = sesion.userId
                _usuarioNombre.value = sesion.nombre
                _usuarioRol.value = sesion.rol
            }
            delay(200)
            _mensajes.value = repositorioMensajes.obtenerPorSolicitud(solicitudId)
            _isLoading.value = false
        }
    }

    fun onTextoChange(texto: String) {
        _textoActual.value = texto
    }

    fun enviarMensaje(solicitudId: String) {
        val texto = _textoActual.value.trim()
        if (texto.isBlank()) return

        viewModelScope.launch {
            val nuevo = Mensaje(
                id = "",
                solicitudId = solicitudId,
                autorId = _usuarioId.value,
                autorNombre = _usuarioNombre.value,
                autorRol = _usuarioRol.value,
                texto = texto
            )
            val guardado = repositorioMensajes.enviar(nuevo)
            _mensajes.value = _mensajes.value + guardado
            _textoActual.value = ""
        }
    }
}
