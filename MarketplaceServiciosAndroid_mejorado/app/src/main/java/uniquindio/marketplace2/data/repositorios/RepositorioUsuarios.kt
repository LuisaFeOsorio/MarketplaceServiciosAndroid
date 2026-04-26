package uniquindio.marketplace2.data.repositorios

import javax.inject.Inject
import javax.inject.Singleton

import uniquindio.marketplace2.data.modelos.ReglaPuntos
import uniquindio.marketplace2.data.modelos.Usuario
import uniquindio.marketplace2.data.modelos.UsuariosMock

// ════════════════════════════════════════════════════════════
//  NUEVO ARCHIVO — RepositorioUsuarios
//  Gestiona los datos del usuario en memoria e implementa
//  la lógica de acumulación de puntos de reputación.
// ════════════════════════════════════════════════════════════
@Singleton
class RepositorioUsuarios @Inject constructor() {

    // Mapa mutable: userId → Usuario
    private val usuarios = mutableMapOf(
        UsuariosMock.clienteEjemplo.id   to UsuariosMock.clienteEjemplo,
        UsuariosMock.trabajadorEjemplo.id to UsuariosMock.trabajadorEjemplo
    )

    // ── Consultas ────────────────────────────────────────────
    fun obtenerPorId(id: String): Usuario? = usuarios[id]

    fun obtenerTodos(): List<Usuario> = usuarios.values.toList()

    // ── Actualización de perfil ──────────────────────────────
    fun actualizar(usuario: Usuario): Boolean {
        if (!usuarios.containsKey(usuario.id)) return false
        usuarios[usuario.id] = usuario
        return true
    }

    // ── Sistema de puntos de reputación ──────────────────────
    /**
     * Suma [cantidad] puntos al usuario con [usuarioId].
     * Si el usuario no existe en el mapa local, la operación
     * es ignorada (no lanza excepción).
     */
    fun acumularPuntos(usuarioId: String, cantidad: Int) {
        val usuario = usuarios[usuarioId] ?: return
        usuarios[usuarioId] = usuario.copy(puntos = usuario.puntos + cantidad)
    }

    /**
     * Registra la finalización de un servicio:
     * • +[ReglaPuntos.POR_SOLICITUD_COMPLETADA] para el proveedor
     * • incrementa serviciosCompletados
     */
    fun registrarServicioCompletado(proveedorId: String) {
        val proveedor = usuarios[proveedorId] ?: return
        usuarios[proveedorId] = proveedor.copy(
            serviciosCompletados = proveedor.serviciosCompletados + 1,
            puntos = proveedor.puntos + ReglaPuntos.POR_SOLICITUD_COMPLETADA
        )
    }

    /**
     * Registra que el proveedor recibió una reseña y acumula puntos.
     */
    fun registrarResenaRecibida(proveedorId: String) {
        acumularPuntos(proveedorId, ReglaPuntos.POR_RESENA_RECIBIDA)
    }

    /** Devuelve los puntos actuales del usuario. */
    fun obtenerPuntos(usuarioId: String): Int = usuarios[usuarioId]?.puntos ?: 0

    /** Nivel de reputación basado en puntos. */
    fun obtenerNivel(puntos: Int): String = when {
        puntos >= 500 -> "Experto ⭐⭐⭐"
        puntos >= 200 -> "Avanzado ⭐⭐"
        puntos >= 50  -> "Regular ⭐"
        else          -> "Nuevo"
    }
}
