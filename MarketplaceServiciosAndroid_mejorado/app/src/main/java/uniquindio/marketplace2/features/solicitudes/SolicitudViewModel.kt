package uniquindio.marketplace2.features.solicitudes

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
import uniquindio.marketplace2.data.modelos.OfertasMock
import uniquindio.marketplace2.data.modelos.Solicitud
import uniquindio.marketplace2.data.repositorios.RepositorioSolicitudes  // ✅ IMPORT AGREGADO
import javax.inject.Inject

@HiltViewModel
class SolicitudViewModel @Inject constructor(
    private val repositorioSolicitudes: RepositorioSolicitudes,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _solicitudes = MutableStateFlow<List<Solicitud>>(emptyList())
    val solicitudes: StateFlow<List<Solicitud>> = _solicitudes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _solicitudEnviada = MutableStateFlow(false)
    val solicitudEnviada: StateFlow<Boolean> = _solicitudEnviada.asStateFlow()

    // ✅ NUEVO: expone el rol del usuario logueado para que la pantalla pueda usarlo
    private val _rolUsuario = MutableStateFlow("")
    val rolUsuario: StateFlow<String> = _rolUsuario.asStateFlow()

    // ✅ CORREGIDO: Carga la sesión real y filtra por rol e ID del usuario logueado
    fun cargarSolicitudesSegunRol() {
        viewModelScope.launch {
            _isLoading.value = true
            val sesion = sessionManager.getUserInfo().first()
            if (sesion != null) {
                _rolUsuario.value = sesion.rol
                _solicitudes.value = when (sesion.rol) {
                    "trabajador" -> repositorioSolicitudes.obtenerPorProveedor(sesion.userId)
                    else         -> repositorioSolicitudes.obtenerPorCliente(sesion.userId)
                }
            }
            _isLoading.value = false
        }
    }

    fun cargarSolicitudesDelTrabajador(proveedorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(300)
            _solicitudes.value = repositorioSolicitudes.obtenerPorProveedor(proveedorId)
            _isLoading.value = false
        }
    }

    fun cargarSolicitudesDelCliente(clienteId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(300)
            _solicitudes.value = repositorioSolicitudes.obtenerPorCliente(clienteId)
            _isLoading.value = false
        }
    }

    fun enviarSolicitud(ofertaId: String, mensaje: String, fechaPreferida: String) {
        if (mensaje.isBlank()) { _error.value = "El mensaje es obligatorio"; return }
        viewModelScope.launch {
            _isLoading.value = true
            // ✅ CORREGIDO: Usa datos del usuario real de la sesión
            val sesion = sessionManager.getUserInfo().first()
            val clienteId = sesion?.userId ?: "1"
            val clienteNombre = sesion?.nombre ?: "Cliente"

            delay(600)
            val ofertaMock = OfertasMock.lista.find { it.id == ofertaId }
            val nueva = Solicitud(
                id = System.currentTimeMillis().toString(),
                ofertaId = ofertaId,
                ofertaTitulo = ofertaMock?.titulo ?: "Servicio",
                clienteId = clienteId,
                clienteNombre = clienteNombre,
                proveedorId = ofertaMock?.proveedorId ?: "2",
                proveedorNombre = ofertaMock?.proveedorNombre ?: "Proveedor",
                fechaSolicitud = "Hoy",
                fechaPreferida = fechaPreferida.ifBlank { "Por definir" },
                mensaje = mensaje,
                estado = "pendiente"
            )
            repositorioSolicitudes.crear(nueva)
            _solicitudEnviada.value = true
            _isLoading.value = false
        }
    }

    fun cambiarEstado(solicitudId: String, nuevoEstado: String) {
        viewModelScope.launch {
            repositorioSolicitudes.cambiarEstado(solicitudId, nuevoEstado)
            _solicitudes.value = _solicitudes.value.map {
                if (it.id == solicitudId) it.copy(estado = nuevoEstado) else it
            }
        }
    }

    fun resetSolicitudEnviada() { _solicitudEnviada.value = false }
    fun limpiarError() { _error.value = null }
}
