package uniquindio.marketplace2.features.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.data.model.Oferta
import uniquindio.marketplace2.data.model.OfertasMock
import javax.inject.Inject

@HiltViewModel
class FeedMapaViewModel @Inject constructor() : ViewModel() {

    private val _ofertas = MutableStateFlow<List<Oferta>>(emptyList())
    val ofertas: StateFlow<List<Oferta>> = _ofertas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _ubicacionActual = MutableStateFlow(UbicacionMock())
    val ubicacionActual: StateFlow<UbicacionMock> = _ubicacionActual.asStateFlow()

    init {
        cargarOfertas()
    }

    fun cargarOfertas() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(800)
            _ofertas.value = OfertasMock.lista
            _isLoading.value = false
        }
    }

    fun obtenerOfertaPorId(id: String): Oferta? {
        return _ofertas.value.find { it.id == id }
    }
}

data class UbicacionMock(
    val lat: Double = 4.7110,
    val lng: Double = -74.0721,
    val nombre: String = "Bogotá"
)