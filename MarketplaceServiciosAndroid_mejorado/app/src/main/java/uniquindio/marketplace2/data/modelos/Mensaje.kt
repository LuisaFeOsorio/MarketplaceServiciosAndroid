package uniquindio.marketplace2.data.modelos

data class Mensaje(
    val id: String,
    val solicitudId: String,
    val autorId: String,
    val autorNombre: String,
    val autorRol: String,       // "cliente" o "trabajador"
    val texto: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    val horaFormateada: String
        get() {
            val diff = System.currentTimeMillis() - timestamp
            return when {
                diff < 60_000 -> "Ahora"
                diff < 3_600_000 -> "Hace ${diff / 60_000} min"
                else -> {
                    val h = (timestamp / 3_600_000 % 24).toInt()
                    val m = (timestamp / 60_000 % 60).toInt()
                    "%02d:%02d".format(h, m)
                }
            }
        }
}

object MensajesMock {
    val lista = mutableListOf(
        Mensaje(
            id = "m1",
            solicitudId = "1",
            autorId = "1",
            autorNombre = "María García",
            autorRol = "cliente",
            texto = "Hola, la fuga está en la tubería debajo del lavaplatos. ¿Cuándo podría venir?",
            timestamp = System.currentTimeMillis() - 3_600_000
        ),
        Mensaje(
            id = "m2",
            solicitudId = "1",
            autorId = "2",
            autorNombre = "Juan Pérez",
            autorRol = "trabajador",
            texto = "Buenos días María. Puedo ir mañana entre 9am y 11am. ¿Le queda bien ese horario?",
            timestamp = System.currentTimeMillis() - 3_000_000
        ),
        Mensaje(
            id = "m3",
            solicitudId = "1",
            autorId = "1",
            autorNombre = "María García",
            autorRol = "cliente",
            texto = "Perfecto, las 9am me viene muy bien. ¡Gracias!",
            timestamp = System.currentTimeMillis() - 1_800_000
        )
    )

    fun obtenerPorSolicitud(solicitudId: String): List<Mensaje> =
        lista.filter { it.solicitudId == solicitudId }.sortedBy { it.timestamp }
}
