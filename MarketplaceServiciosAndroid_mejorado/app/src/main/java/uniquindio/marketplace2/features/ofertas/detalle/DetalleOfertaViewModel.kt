package uniquindio.marketplace2.features.ofertas.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.data.model.Oferta
import uniquindio.marketplace2.data.repositorios.RepositorioOfertas  // ✅ IMPORT AGREGADO
import javax.inject.Inject

@HiltViewModel
class DetalleOfertaViewModel @Inject constructor(
    private val repositorioOfertas: RepositorioOfertas
) : ViewModel() {

    private val _oferta = MutableStateFlow<Oferta?>(null)
    val oferta = _oferta.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun cargarOferta(ofertaId: String) {
        // ✅ CORREGIDO: Evita relanzar si ya cargó la misma oferta
        if (_oferta.value?.id == ofertaId && _oferta.value != null) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            delay(300)
            val encontrada = repositorioOfertas.obtenerPorId(ofertaId)
            if (encontrada != null) {
                _oferta.value = encontrada
            } else {
                // ✅ CORREGIDO: En vez de lanzar IllegalArgumentException,
                //    seteamos el error para que la pantalla lo maneje graciosamente
                _error.value = "No se encontró la oferta solicitada"
            }
            _isLoading.value = false
        }
    }

    fun limpiarError() { _error.value = null }
}
