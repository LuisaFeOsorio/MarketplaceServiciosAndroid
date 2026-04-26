package uniquindio.marketplace2.features.ofertas.crear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.data.model.Oferta
import uniquindio.marketplace2.data.repositorios.RepositorioOfertas  // ✅ IMPORT AGREGADO
import javax.inject.Inject

@HiltViewModel
class CrearOfertaViewModel @Inject constructor(
    private val repositorioOfertas: RepositorioOfertas
) : ViewModel() {

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    private val _categoria = MutableStateFlow("Hogar")
    val categoria: StateFlow<String> = _categoria.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()

    private val _precioMin = MutableStateFlow("")
    val precioMin: StateFlow<String> = _precioMin.asStateFlow()

    private val _precioMax = MutableStateFlow("")
    val precioMax: StateFlow<String> = _precioMax.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _ofertaCreada = MutableStateFlow<Oferta?>(null)
    val ofertaCreada: StateFlow<Oferta?> = _ofertaCreada.asStateFlow()

    val categorias = listOf("Hogar", "Educación", "Mascotas", "Tecnología", "Transporte")

    fun onTituloChange(v: String) { _titulo.value = v; _error.value = null }
    fun onCategoriaChange(v: String) { _categoria.value = v }
    fun onDescripcionChange(v: String) { _descripcion.value = v; _error.value = null }
    fun onPrecioMinChange(v: String) { _precioMin.value = v }
    fun onPrecioMaxChange(v: String) { _precioMax.value = v }

    fun publicarOferta(proveedorId: String, proveedorNombre: String) {
        if (_titulo.value.isBlank()) { _error.value = "El título es obligatorio"; return }
        if (_descripcion.value.length < 10) { _error.value = "La descripción debe tener al menos 10 caracteres"; return }
        val min = _precioMin.value.toDoubleOrNull()
        val max = _precioMax.value.toDoubleOrNull()
        if (min == null || max == null) { _error.value = "Los precios deben ser números válidos"; return }
        if (max <= min) { _error.value = "El precio máximo debe ser mayor al mínimo"; return }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            delay(800)
            val nueva = Oferta(
                id = System.currentTimeMillis().toString(),
                titulo = _titulo.value.trim(),
                categoria = _categoria.value,
                descripcion = _descripcion.value.trim(),
                precioMin = min,
                precioMax = max,
                proveedorId = proveedorId,
                proveedorNombre = proveedorNombre,
                distanciaKm = 0.0,
                estado = "pendiente"
            )
            repositorioOfertas.crear(nueva)
            _ofertaCreada.value = nueva
            _isLoading.value = false
        }
    }

    fun resetOfertaCreada() { _ofertaCreada.value = null }
    fun limpiarError() { _error.value = null }
}
