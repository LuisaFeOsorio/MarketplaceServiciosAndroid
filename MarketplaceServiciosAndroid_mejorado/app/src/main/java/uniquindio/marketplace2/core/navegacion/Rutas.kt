package uniquindio.marketplace2.core.navegacion



// ── Autenticación ──────────────────────────────────────────
@kotlinx.serialization.Serializable
object Login

@kotlinx.serialization.Serializable
object Registro

@kotlinx.serialization.Serializable
object Feed

// ── Principal ─────────────────────────────────────────────
@kotlinx.serialization.Serializable
data class Home(val rol: String)

// ── Ofertas ───────────────────────────────────────────────
@kotlinx.serialization.Serializable
data class DetalleOferta(val ofertaId: String)

@kotlinx.serialization.Serializable
object CrearOferta

@kotlinx.serialization.Serializable
data class EditarOferta(val ofertaId: String)

// ── Solicitudes ───────────────────────────────────────────
@kotlinx.serialization.Serializable
data class SolicitarServicio(val ofertaId: String)

@kotlinx.serialization.Serializable
object Solicitudes

@kotlinx.serialization.Serializable
data class DetalleSolicitud(val solicitudId: String)

// ── Perfil ────────────────────────────────────────────────
@kotlinx.serialization.Serializable
object Perfil

// ── Feed / Búsqueda ───────────────────────────────────────
@kotlinx.serialization.Serializable
object Buscar

// ── Trabajador ────────────────────────────────────────────
@kotlinx.serialization.Serializable
object Dashboard

@kotlinx.serialization.Serializable
object MisOfertas

// ── Notificaciones ────────────────────────────────────────
@kotlinx.serialization.Serializable
object Notificaciones

// ── Moderador ─────────────────────────────────────────────
@kotlinx.serialization.Serializable
object PanelModerador

// ── Recuperar contraseña ──────────────────────────────────
@kotlinx.serialization.Serializable
object OlvidarContrasena

@kotlinx.serialization.Serializable
data class RecuperarContrasena(val email: String)
