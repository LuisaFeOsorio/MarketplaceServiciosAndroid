package uniquindio.marketplace2.data.modelos

import java.util.Date

data class Notificacion(
    val id: String,
    val titulo: String,
    val mensaje: String,
    val fecha: Long,
    val leida: Boolean = false,
    val tipo: TipoNotificacion,
    val relacionId: String? = null,
    val imagenUrl: String? = null
) {

    val fechaFormateada: String
        get() = formatFecha(fecha)

    companion object {

        fun formatFecha(timestamp: Long): String {
            val ahora = System.currentTimeMillis()
            val diff = ahora - timestamp

            return when {
                diff < 60_000 -> "Hace ${diff / 1000} segundos"
                diff < 3_600_000 -> "Hace ${diff / 60_000} minutos"
                diff < 86_400_000 -> "Hace ${diff / 3_600_000} horas"
                diff < 7_864_800_000 -> "Hace ${diff / 86_400_000} días"
                else -> {
                    val date = Date(timestamp)
                    "${date.date}/${date.month + 1}/${date.year + 1900}"
                }
            }
        }
    }
}

enum class TipoNotificacion(
    val valor: String,
    val iconoRes: Int? = null // Para usar con íconos después
) {
    SOLICITUD_NUEVA("solicitud_nueva"),
    SOLICITUD_ACEPTADA("solicitud_aceptada"),
    SOLICITUD_RECHAZADA("solicitud_rechazada"),
    SERVICIO_COMPLETADO("servicio_completado"),
    MENSAJE_NUEVO("mensaje_nuevo"),
    OFERTA_VERIFICADA("oferta_verificada"),
    OFERTA_RECHAZADA("oferta_rechazada"),
    MODERACION_ALERTA("moderacion_alerta")
}

object NotificacionMock {
    val lista = listOf(
        Notificacion(
            id = "1",
            titulo = "Nueva solicitud de servicio",
            mensaje = "María García ha solicitado tu servicio de reparación de fugas",
            fecha = System.currentTimeMillis() - 3_600_000, // hace 1 hora
            leida = false,
            tipo = TipoNotificacion.SOLICITUD_NUEVA,
            relacionId = "1"
        ),
        Notificacion(
            id = "2",
            titulo = "Solicitud aceptada",
            mensaje = "Juan Pérez aceptó tu solicitud para Instalaciones Eléctricas",
            fecha = System.currentTimeMillis() - 86_400_000, // hace 1 día
            leida = true,
            tipo = TipoNotificacion.SOLICITUD_ACEPTADA,
            relacionId = "2"
        ),
        Notificacion(
            id = "3",
            titulo = "Tu oferta ha sido verificada",
            mensaje = "Tu servicio 'Clases de Matemáticas' ha sido aprobado y ya está visible",
            fecha = System.currentTimeMillis() - 172_800_000, // hace 2 días
            leida = true,
            tipo = TipoNotificacion.OFERTA_VERIFICADA,
            relacionId = "3"
        ),
        Notificacion(
            id = "4",
            titulo = "Nuevo mensaje",
            mensaje = "Carlos Ruiz te envió un mensaje sobre tu oferta",
            fecha = System.currentTimeMillis() - 7_200_000, // hace 2 horas
            leida = false,
            tipo = TipoNotificacion.MENSAJE_NUEVO,
            relacionId = "4"
        ),
        Notificacion(
            id = "5",
            titulo = "Servicio completado",
            mensaje = "El servicio 'Reparación de Fugas' ha sido marcado como completado",
            fecha = System.currentTimeMillis() - 259_200_000, // hace 3 días
            leida = true,
            tipo = TipoNotificacion.SERVICIO_COMPLETADO,
            relacionId = "1"
        )
    )

    fun obtenerNoLeidas(): List<Notificacion> {
        return lista.filter { !it.leida }
    }

    fun obtenerPorTipo(tipo: TipoNotificacion): List<Notificacion> {
        return lista.filter { it.tipo == tipo }
    }

    fun obtenerRecientes(): List<Notificacion> {
        val hace24Horas = System.currentTimeMillis() - 86_400_000
        return lista.filter { it.fecha > hace24Horas }
    }

    fun agruparPorDia(): Map<String, List<Notificacion>> {
        val hoy = System.currentTimeMillis()
        val ayer = hoy - 86_400_000
        val estaSemana = hoy - 7 * 86_400_000

        return lista.groupBy { notificacion ->
            when {
                notificacion.fecha > hoy -> "Hoy"
                notificacion.fecha > ayer -> "Ayer"
                notificacion.fecha > estaSemana -> "Esta semana"
                else -> "Anterior"
            }
        }
    }
}