package uniquindio.marketplace2.data.repositorios

import javax.inject.Inject
import javax.inject.Singleton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import uniquindio.marketplace2.data.modelos.Comentario
import uniquindio.marketplace2.data.modelos.Oferta
import uniquindio.marketplace2.data.modelos.OfertasMock

@Singleton
class RepositorioOfertas @Inject constructor(
    private val repositorioUsuarios: RepositorioUsuarios
) {

    private val ofertas = OfertasMock.lista

    fun obtenerTodas(): List<Oferta> = ofertas.toList()

    fun obtenerActivas(): List<Oferta> = ofertas.filter { it.estado == "activa" }

    fun obtenerPorCategoria(categoria: String): List<Oferta> =
        if (categoria == "Todos") obtenerActivas()
        else ofertas.filter { it.categoria == categoria && it.estado == "activa" }

    fun obtenerPorProveedor(proveedorId: String): List<Oferta> =
        ofertas.filter { it.proveedorId == proveedorId }

    fun obtenerPorId(id: String): Oferta? = ofertas.find { it.id == id }

    fun crear(oferta: Oferta): Oferta {
        val nueva = oferta.copy(
            id = System.currentTimeMillis().toString(),
            estado = "pendiente"
        )
        ofertas.add(nueva)
        repositorioUsuarios.acumularPuntos(oferta.proveedorId, uniquindio.marketplace2.data.modelos.ReglaPuntos.POR_OFERTA_CREADA)
        return nueva
    }

    fun actualizar(oferta: Oferta): Boolean {
        val index = ofertas.indexOfFirst { it.id == oferta.id }
        return if (index >= 0) {
            ofertas[index] = oferta
            true
        } else false
    }

    fun cambiarEstado(id: String, nuevoEstado: String): Boolean {
        val index = ofertas.indexOfFirst { it.id == id }
        return if (index >= 0) {
            ofertas[index] = ofertas[index].copy(estado = nuevoEstado)
            true
        } else false
    }

    fun eliminar(id: String): Boolean = ofertas.removeIf { it.id == id }

    fun buscar(query: String): List<Oferta> =
        ofertas.filter { oferta ->
            oferta.titulo.contains(query, ignoreCase = true) ||
                    oferta.descripcion.contains(query, ignoreCase = true) ||
                    oferta.categoria.contains(query, ignoreCase = true) ||
                    oferta.proveedorNombre.contains(query, ignoreCase = true)
        }

    fun votar(ofertaId: String, usuarioId: String): Boolean {
        val index = ofertas.indexOfFirst { it.id == ofertaId }
        if (index < 0) return false
        val oferta = ofertas[index]
        if (usuarioId in oferta.usuariosQueVotaron) return false

        ofertas[index] = oferta.copy(
            votos = oferta.votos + 1,
            usuariosQueVotaron = oferta.usuariosQueVotaron + usuarioId
        )

        repositorioUsuarios.acumularPuntos(oferta.proveedorId, uniquindio.marketplace2.data.modelos.ReglaPuntos.POR_VOTO_RECIBIDO)
        return true
    }

    /**
     * Quita el voto de [usuarioId] en la oferta [ofertaId].
     * Retorna true si se quitó, false si el usuario no había votado.
     */
    fun quitarVoto(ofertaId: String, usuarioId: String): Boolean {
        val index = ofertas.indexOfFirst { it.id == ofertaId }
        if (index < 0) return false
        val oferta = ofertas[index]
        if (usuarioId !in oferta.usuariosQueVotaron) return false

        ofertas[index] = oferta.copy(
            votos = (oferta.votos - 1).coerceAtLeast(0),
            usuariosQueVotaron = oferta.usuariosQueVotaron - usuarioId
        )
        return true
    }

    /** Indica si [usuarioId] ya votó la oferta [ofertaId]. */
    fun haVotado(ofertaId: String, usuarioId: String): Boolean =
        ofertas.find { it.id == ofertaId }?.usuariosQueVotaron?.contains(usuarioId) == true

    // ── NUEVO: Comentarios ────────────────────────────────────
    /**
     * Agrega un comentario de texto a la oferta [ofertaId].
     * Retorna el Comentario creado o null si la oferta no existe.
     */
    fun agregarComentario(
        ofertaId: String,
        autorId: String,
        autorNombre: String,
        texto: String
    ): Comentario? {
        val index = ofertas.indexOfFirst { it.id == ofertaId }
        if (index < 0) return null

        val nuevo = Comentario(
            id    = "cmt_${System.currentTimeMillis()}",
            autorId = autorId,
            autorNombre = autorNombre,
            texto = texto,
            fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        )
        val oferta = ofertas[index]
        ofertas[index] = oferta.copy(comentarios = oferta.comentarios + nuevo)

        // El autor gana puntos por comentar
        repositorioUsuarios.acumularPuntos(autorId, uniquindio.marketplace2.data.modelos.ReglaPuntos.POR_COMENTARIO_PUBLICADO)
        return nuevo
    }

    fun obtenerComentarios(ofertaId: String): List<Comentario> =
        ofertas.find { it.id == ofertaId }?.comentarios ?: emptyList()
}
