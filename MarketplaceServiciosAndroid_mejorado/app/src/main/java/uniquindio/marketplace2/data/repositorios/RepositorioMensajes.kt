package uniquindio.marketplace2.data.repositorios

import javax.inject.Inject
import javax.inject.Singleton
import uniquindio.marketplace2.data.modelos.Mensaje
import uniquindio.marketplace2.data.modelos.MensajesMock

@Singleton
class RepositorioMensajes @Inject constructor() {

    private val mensajes = MensajesMock.lista

    fun obtenerPorSolicitud(solicitudId: String): List<Mensaje> =
        mensajes.filter { it.solicitudId == solicitudId }.sortedBy { it.timestamp }

    fun enviar(mensaje: Mensaje): Mensaje {
        val nuevo = mensaje.copy(
            id = "m${System.currentTimeMillis()}",
            timestamp = System.currentTimeMillis()
        )
        mensajes.add(nuevo)
        return nuevo
    }
}
