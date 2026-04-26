package uniquindio.marketplace2.data.repositorios

import javax.inject.Inject
import javax.inject.Singleton

import uniquindio.marketplace2.data.modelos.Solicitud
import uniquindio.marketplace2.data.modelos.SolicitudMock

@Singleton
class RepositorioSolicitudes @Inject constructor() {

    private val solicitudes = SolicitudMock.lista.toMutableList()

    fun obtenerTodas(): List<Solicitud> = solicitudes.toList()

    fun obtenerPorCliente(clienteId: String): List<Solicitud> =
        solicitudes.filter { it.clienteId == clienteId }

    fun obtenerPorProveedor(proveedorId: String): List<Solicitud> =
        solicitudes.filter { it.proveedorId == proveedorId }

    fun obtenerPorId(id: String): Solicitud? = solicitudes.find { it.id == id }

    fun obtenerPorEstado(estado: String): List<Solicitud> =
        solicitudes.filter { it.estado == estado }

    fun crear(solicitud: Solicitud): Solicitud {
        val nueva = solicitud.copy(
            id = System.currentTimeMillis().toString(),
            estado = "pendiente"
        )
        solicitudes.add(nueva)
        return nueva
    }

    fun cambiarEstado(id: String, nuevoEstado: String): Boolean {
        val index = solicitudes.indexOfFirst { it.id == id }
        return if (index >= 0) {
            solicitudes[index] = solicitudes[index].copy(estado = nuevoEstado)
            true
        } else false
    }
}
