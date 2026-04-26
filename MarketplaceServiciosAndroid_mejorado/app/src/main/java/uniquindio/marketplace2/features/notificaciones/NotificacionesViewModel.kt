package uniquindio.marketplace2.features.notificaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.data.modelos.Notificacion
import uniquindio.marketplace2.data.modelos.NotificacionMock
import javax.inject.Inject

@HiltViewModel
class NotificacionesViewModel @Inject constructor() : ViewModel() {

    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _noLeidas = MutableStateFlow(0)
    val noLeidas: StateFlow<Int> = _noLeidas.asStateFlow()

    init {
        cargarNotificaciones()
    }

    fun cargarNotificaciones() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500)
            _notificaciones.value = NotificacionMock.lista
            _noLeidas.value = _notificaciones.value.count { !it.leida }
            _isLoading.value = false
        }
    }

    fun marcarComoLeida(notificacionId: String) {
        _notificaciones.value = _notificaciones.value.map { notif ->
            if (notif.id == notificacionId && !notif.leida) {
                _noLeidas.value--
                notif.copy(leida = true)
            } else notif
        }
    }

    fun marcarTodasComoLeidas() {
        _notificaciones.value = _notificaciones.value.map { it.copy(leida = true) }
        _noLeidas.value = 0
    }

    fun eliminarNotificacion(notificacionId: String) {
        val notifAEliminar = _notificaciones.value.find { it.id == notificacionId }
        if (notifAEliminar != null && !notifAEliminar.leida) _noLeidas.value--
        _notificaciones.value = _notificaciones.value.filter { it.id != notificacionId }
    }
}