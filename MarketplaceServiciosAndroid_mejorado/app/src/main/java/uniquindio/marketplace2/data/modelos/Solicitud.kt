package uniquindio.marketplace2.data.modelos

data class Solicitud(
    val id: String,
    val ofertaId: String,
    val ofertaTitulo: String,
    val clienteId: String = "",
    val clienteNombre: String,
    val proveedorId: String = "2",
    val proveedorNombre: String = "Juan Pérez",
    val fechaSolicitud: String,
    val fechaPreferida: String,
    val mensaje: String,
    val estado: String // pendiente, aceptada, completada, rechazada
)

object SolicitudMock {
    val lista = mutableListOf(
        Solicitud(
            id = "1", ofertaId = "1",
            ofertaTitulo = "Reparación de Fugas 24/7",
            clienteId = "1", clienteNombre = "María García",
            proveedorId = "2", proveedorNombre = "Juan Pérez",
            fechaSolicitud = "15/03/2024", fechaPreferida = "16/03/2024",
            mensaje = "Necesito ayuda urgente con una fuga en la cocina, ya lleva 2 días.",
            estado = "pendiente"
        ),
        Solicitud(
            id = "2", ofertaId = "4",
            ofertaTitulo = "Instalaciones Eléctricas Residenciales",
            clienteId = "5", clienteNombre = "Carlos Ruiz",
            proveedorId = "2", proveedorNombre = "Juan Pérez",
            fechaSolicitud = "14/03/2024", fechaPreferida = "17/03/2024",
            mensaje = "Quiero cambiar todos los tomacorrientes de mi apartamento.",
            estado = "pendiente"
        ),
        Solicitud(
            id = "3", ofertaId = "1",
            ofertaTitulo = "Reparación de Fugas 24/7",
            clienteId = "1", clienteNombre = "Ana López",
            proveedorId = "2", proveedorNombre = "Juan Pérez",
            fechaSolicitud = "10/03/2024", fechaPreferida = "12/03/2024",
            mensaje = "Fuga en el baño principal, gotea constantemente.",
            estado = "aceptada"
        ),
        Solicitud(
            id = "4", ofertaId = "1",
            ofertaTitulo = "Reparación de Fugas 24/7",
            clienteId = "6", clienteNombre = "Pedro Herrera",
            proveedorId = "2", proveedorNombre = "Juan Pérez",
            fechaSolicitud = "05/03/2024", fechaPreferida = "06/03/2024",
            mensaje = "Problema con la tubería del lavamanos.",
            estado = "completada"
        )
    )
}
