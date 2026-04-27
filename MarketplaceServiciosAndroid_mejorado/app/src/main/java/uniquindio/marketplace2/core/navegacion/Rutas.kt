package uniquindio.marketplace2.core.navegacion



// ========== PANTALLAS DE AUTENTICACIÓN ==========
@kotlinx.serialization.Serializable
object Login

@kotlinx.serialization.Serializable
object Registro
@kotlinx.serialization.Serializable
object Feed

// ========== PANTALLA PRINCIPAL ==========
@kotlinx.serialization.Serializable
data class Home(val rol: String)

// ========== PANTALLAS DE OFERTAS ==========
@kotlinx.serialization.Serializable
data class DetalleOferta(val ofertaId: String)

@kotlinx.serialization.Serializable
object CrearOferta

@kotlinx.serialization.Serializable
data class EditarOferta(val ofertaId: String)

// ========== PANTALLAS DE SOLICITUDES ==========
@kotlinx.serialization.Serializable
data class SolicitarServicio(val ofertaId: String)
@kotlinx.serialization.Serializable
object Solicitudes

@kotlinx.serialization.Serializable
data class DetalleSolicitud(val solicitudId: String)

// ========== PANTALLAS DE MENSAJES ==========
@kotlinx.serialization.Serializable
data class Mensajes(val solicitudId: String, val tituloServicio: String)

// ========== PANTALLAS DE PERFIL ==========
@kotlinx.serialization.Serializable
object Perfil

// ========== PANTALLAS DE FEED ==========
@kotlinx.serialization.Serializable
object Buscar

// ========== PANTALLAS DE TRABAJADOR ==========
@kotlinx.serialization.Serializable
object Dashboard

@kotlinx.serialization.Serializable
object MisOfertas

// ========== PANTALLAS DE NOTIFICACIONES ==========
@kotlinx.serialization.Serializable
object Notificaciones

// ========== PANTALLAS DE MODERADOR ==========
@kotlinx.serialization.Serializable
object PanelModerador

@kotlinx.serialization.Serializable
object HistorialModerador
// ========== PANTALLAS DE RECUPERAR CONTRASEÑA ==========
@kotlinx.serialization.Serializable
object OlvidarContrasena

@kotlinx.serialization.Serializable
data class RecuperarContrasena(val email: String)
