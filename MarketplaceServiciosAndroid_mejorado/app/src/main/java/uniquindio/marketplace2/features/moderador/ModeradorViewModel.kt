package uniquindio.marketplace2.features.moderador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.data.modelos.OfertaPendiente
import uniquindio.marketplace2.data.modelos.OfertasPendientesMock
import uniquindio.marketplace2.data.repositorios.RepositorioOfertas
import javax.inject.Inject

@HiltViewModel
class ModeradorViewModel @Inject constructor(
    private val repositorioOfertas: RepositorioOfertas
) : ViewModel() {

    private val _ofertasPendientes = MutableStateFlow<List<OfertaPendiente>>(emptyList())
    val ofertasPendientes: StateFlow<List<OfertaPendiente>> = _ofertasPendientes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _ofertaSeleccionada = MutableStateFlow<OfertaPendiente?>(null)
    val ofertaSeleccionada: StateFlow<OfertaPendiente?> = _ofertaSeleccionada.asStateFlow()

    private val _mostrarDialogoRechazo = MutableStateFlow(false)
    val mostrarDialogoRechazo: StateFlow<Boolean> = _mostrarDialogoRechazo.asStateFlow()

    private val _motivoRechazo = MutableStateFlow("")
    val motivoRechazo: StateFlow<String> = _motivoRechazo.asStateFlow()

    private val _accionExitosa = MutableStateFlow(false)
    val accionExitosa: StateFlow<Boolean> = _accionExitosa.asStateFlow()

    init { cargarPendientes() }

    fun cargarPendientes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            delay(500)
            _ofertasPendientes.value = OfertasPendientesMock.lista.toMutableList()
            _isLoading.value = false
        }
    }

    fun seleccionarOferta(oferta: OfertaPendiente) { _ofertaSeleccionada.value = oferta }
    fun limpiarSeleccion() { _ofertaSeleccionada.value = null }

    fun abrirDialogoRechazo() {
        _motivoRechazo.value = ""
        _error.value = null
        _mostrarDialogoRechazo.value = true
    }

    fun cerrarDialogoRechazo() {
        _mostrarDialogoRechazo.value = false
        _motivoRechazo.value = ""
        _error.value = null
    }

    fun onMotivoRechazoChange(valor: String) {
        _motivoRechazo.value = valor
        _error.value = null
    }

    fun aprobarOferta(ofertaId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(400)
            repositorioOfertas.cambiarEstado(ofertaId, "activa")
            _ofertasPendientes.value = _ofertasPendientes.value.filter { it.id != ofertaId }
            _ofertaSeleccionada.value = null
            _accionExitosa.value = true
            _isLoading.value = false
        }
    }

    fun rechazarOferta(ofertaId: String) {
        if (_motivoRechazo.value.isBlank()) {
            _error.value = "Debes indicar el motivo del rechazo"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            delay(400)
            repositorioOfertas.cambiarEstado(ofertaId, "rechazada")
            _ofertasPendientes.value = _ofertasPendientes.value.filter { it.id != ofertaId }
            _ofertaSeleccionada.value = null
            _mostrarDialogoRechazo.value = false
            _motivoRechazo.value = ""
            _accionExitosa.value = true
            _isLoading.value = false
        }
    }

    fun resetAccionExitosa() { _accionExitosa.value = false }
    fun limpiarError() { _error.value = null }
}
