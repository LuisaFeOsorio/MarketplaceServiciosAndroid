package uniquindio.marketplace2.features.ofertas.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uniquindio.marketplace2.core.datastore.SessionManager
import uniquindio.marketplace2.data.modelos.Comentario
import uniquindio.marketplace2.data.modelos.Oferta
import uniquindio.marketplace2.data.repositorios.RepositorioOfertas
import javax.inject.Inject

// ════════════════════════════════════════════════════════════
//  CAMBIOS RESPECTO AL ORIGINAL:
//  • Inyecta SessionManager para conocer el usuario activo
//  • haVotado: StateFlow<Boolean>      → estado del botón "Me interesa"
//  • comentarios: StateFlow<List<Comentario>>
//  • textoComentario: StateFlow<String> → input del usuario
//  • votar() / quitarVoto()            → toggle de voto
//  • publicarComentario()              → envía comentario
// ════════════════════════════════════════════════════════════
@HiltViewModel
class DetalleOfertaViewModel @Inject constructor(
    private val repositorioOfertas: RepositorioOfertas,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _oferta = MutableStateFlow<Oferta?>(null)
    val oferta = _oferta.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // ── NUEVO: Votos ─────────────────────────────────────────
    private val _haVotado = MutableStateFlow(false)
    val haVotado = _haVotado.asStateFlow()

    private val _mensajeVoto = MutableStateFlow<String?>(null)
    val mensajeVoto = _mensajeVoto.asStateFlow()

    // ── NUEVO: Comentarios ────────────────────────────────────
    private val _comentarios = MutableStateFlow<List<Comentario>>(emptyList())
    val comentarios = _comentarios.asStateFlow()

    private val _textoComentario = MutableStateFlow("")
    val textoComentario = _textoComentario.asStateFlow()

    private val _enviandoComentario = MutableStateFlow(false)
    val enviandoComentario = _enviandoComentario.asStateFlow()

    // ID del usuario activo (cargado al iniciar)
    private var usuarioActivoId: String = ""
    private var usuarioActivoNombre: String = ""

    init {
        viewModelScope.launch {
            sessionManager.getUserInfo().collect { sesion ->
                usuarioActivoId = sesion?.userId ?: ""
                usuarioActivoNombre = sesion?.nombre ?: "Usuario"
            }
        }
    }

    // ── Carga inicial ─────────────────────────────────────────
    fun cargarOferta(ofertaId: String) {
        if (_oferta.value?.id == ofertaId && _oferta.value != null) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            delay(300)
            val encontrada = repositorioOfertas.obtenerPorId(ofertaId)
            if (encontrada != null) {
                _oferta.value = encontrada
                _comentarios.value = encontrada.comentarios
                // Verificar si el usuario ya votó
                _haVotado.value = repositorioOfertas.haVotado(ofertaId, usuarioActivoId)
            } else {
                _error.value = "No se encontró la oferta solicitada"
            }
            _isLoading.value = false
        }
    }

    // ── NUEVO: Toggle de voto "Me interesa / Es importante" ──
    fun toggleVoto() {
        val ofertaActual = _oferta.value ?: return
        if (usuarioActivoId.isBlank()) {
            _mensajeVoto.value = "Debes iniciar sesión para votar"
            return
        }
        viewModelScope.launch {
            if (_haVotado.value) {
                repositorioOfertas.quitarVoto(ofertaActual.id, usuarioActivoId)
                _haVotado.value = false
                _mensajeVoto.value = "Voto retirado"
            } else {
                repositorioOfertas.votar(ofertaActual.id, usuarioActivoId)
                _haVotado.value = true
                _mensajeVoto.value = "¡Marcado como importante!"
            }
            // Refresca la oferta para mostrar el nuevo conteo
            _oferta.value = repositorioOfertas.obtenerPorId(ofertaActual.id)
        }
    }

    fun limpiarMensajeVoto() { _mensajeVoto.value = null }

    // ── NUEVO: Comentarios ────────────────────────────────────
    fun onTextoComentarioChange(texto: String) { _textoComentario.value = texto }

    fun publicarComentario() {
        val texto = _textoComentario.value.trim()
        val ofertaActual = _oferta.value ?: return
        if (texto.isBlank()) return
        if (usuarioActivoId.isBlank()) {
            _error.value = "Debes iniciar sesión para comentar"
            return
        }
        viewModelScope.launch {
            _enviandoComentario.value = true
            delay(200)
            val nuevo = repositorioOfertas.agregarComentario(
                ofertaId    = ofertaActual.id,
                autorId     = usuarioActivoId,
                autorNombre = usuarioActivoNombre,
                texto       = texto
            )
            if (nuevo != null) {
                _comentarios.value = repositorioOfertas.obtenerComentarios(ofertaActual.id)
                _textoComentario.value = ""
            }
            _enviandoComentario.value = false
        }
    }

    fun limpiarError() { _error.value = null }
}
