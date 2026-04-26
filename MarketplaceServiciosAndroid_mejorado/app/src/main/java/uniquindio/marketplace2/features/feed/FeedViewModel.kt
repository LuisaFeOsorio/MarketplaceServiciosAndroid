package uniquindio.marketplace2.features.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.data.modelos.Oferta
import uniquindio.marketplace2.data.repositorios.RepositorioOfertas  // ✅ IMPORT AGREGADO
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repositorioOfertas: RepositorioOfertas
) : ViewModel() {

    private val _ofertas = MutableStateFlow<List<Oferta>>(emptyList())
    val ofertas: StateFlow<List<Oferta>> = _ofertas.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow("Todos")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada.asStateFlow()

    val categorias = listOf("Todos", "Hogar", "Educación", "Mascotas", "Tecnología", "Transporte")

    init { cargarOfertas() }

    fun cargarOfertas() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(500)
            _ofertas.value = repositorioOfertas.obtenerPorCategoria(_categoriaSeleccionada.value)
            _isLoading.value = false
        }
    }

    fun filtrarPorCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
        viewModelScope.launch {
            _isLoading.value = true
            delay(200)
            _ofertas.value = repositorioOfertas.obtenerPorCategoria(categoria)
            _isLoading.value = false
        }
    }

    fun buscar(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(200)
            _ofertas.value = if (query.isBlank()) repositorioOfertas.obtenerActivas()
            else repositorioOfertas.buscar(query)
            _isLoading.value = false
        }
    }
}
