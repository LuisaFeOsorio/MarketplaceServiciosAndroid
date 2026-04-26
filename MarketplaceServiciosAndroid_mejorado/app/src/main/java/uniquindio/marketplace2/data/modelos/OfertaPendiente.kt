package uniquindio.marketplace2.data.modelos

/**
 * Representa una oferta enviada por un trabajador que está esperando
 * revisión por parte del moderador.
 *
 * [analisisIA] es opcional: en esta entrega se rellena con datos mock
 * pero no requiere integración real con ningún servicio de IA.
 */
data class OfertaPendiente(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val precioMin: Double,
    val precioMax: Double,
    val proveedorId: String,
    val proveedorNombre: String,
    /** Timestamp en milisegundos desde epoch */
    val fechaCreacion: Long,
    val estado: String = "pendiente",
    val analisisIA: AnalisisIA? = null
)

/**
 * Resultado simulado de un análisis de contenido.
 * No requiere implementación real de IA en esta entrega.
 *
 * @param tieneLenguajeInapropiado si el texto superó el umbral de contenido inadecuado
 * @param palabrasDetectadas       lista de términos problemáticos encontrados
 * @param versionSugerida          descripción alternativa sugerida (puede estar vacía)
 * @param puntajeConfianza         valor entre 0.0 y 1.0 que indica certeza del análisis
 * @param recomendacion            "aprobar" | "revisar" | "rechazar"
 */
data class AnalisisIA(
    val tieneLenguajeInapropiado: Boolean,
    val palabrasDetectadas: List<String>,
    val versionSugerida: String,
    val puntajeConfianza: Float,
    val recomendacion: String
)

object OfertasPendientesMock {
    val lista: List<OfertaPendiente> = listOf(
        OfertaPendiente(
            id = "p1",
            titulo = "Reparación de Fugas 24/7",
            descripcion = "Soy el mejor plomero de la ciudad, los demás son unos chapuceros " +
                    "que no saben nada. Contrátame y verás la diferencia.",
            categoria = "Hogar",
            precioMin = 40.0,
            precioMax = 80.0,
            proveedorId = "2",
            proveedorNombre = "Juan Pérez",
            fechaCreacion = System.currentTimeMillis() - 3_600_000L,
            analisisIA = AnalisisIA(
                tieneLenguajeInapropiado = true,
                palabrasDetectadas = listOf("chapuceros", "no saben nada"),
                versionSugerida = "Plomero profesional con experiencia en detección y " +
                        "reparación de fugas. Trabajo garantizado y precios competitivos.",
                puntajeConfianza = 0.92f,
                recomendacion = "revisar"
            )
        ),
        OfertaPendiente(
            id = "p2",
            titulo = "Clases de Matemáticas",
            descripcion = "Profesor con 10 años de experiencia ofrece clases particulares. " +
                    "Metodología efectiva y resultados garantizados para todos los niveles.",
            categoria = "Educación",
            precioMin = 25.0,
            precioMax = 50.0,
            proveedorId = "4",
            proveedorNombre = "María González",
            fechaCreacion = System.currentTimeMillis() - 86_400_000L,
            analisisIA = AnalisisIA(
                tieneLenguajeInapropiado = false,
                palabrasDetectadas = emptyList(),
                versionSugerida = "",
                puntajeConfianza = 0.98f,
                recomendacion = "aprobar"
            )
        ),
        OfertaPendiente(
            id = "p3",
            titulo = "Paseo de Mascotas",
            descripcion = "No me importa si tu perro es agresivo, yo puedo con cualquiera. " +
                    "Los dueños son los que no saben educar a sus mascotas.",
            categoria = "Mascotas",
            precioMin = 15.0,
            precioMax = 30.0,
            proveedorId = "5",
            proveedorNombre = "Carlos Ruiz",
            fechaCreacion = System.currentTimeMillis() - 172_800_000L,
            analisisIA = AnalisisIA(
                tieneLenguajeInapropiado = true,
                palabrasDetectadas = listOf("no me importa", "no saben educar"),
                versionSugerida = "Experto en cuidado de mascotas de todas las razas y edades. " +
                        "Paseos responsables con reporte fotográfico incluido.",
                puntajeConfianza = 0.95f,
                recomendacion = "revisar"
            )
        ),
        OfertaPendiente(
            id = "p4",
            titulo = "Diseño de Interiores",
            descripcion = "Diseñadora de interiores titulada con portafolio en Instagram. " +
                    "Transformo espacios residenciales y comerciales con presupuestos accesibles.",
            categoria = "Hogar",
            precioMin = 150.0,
            precioMax = 400.0,
            proveedorId = "15",
            proveedorNombre = "Valentina Ríos",
            fechaCreacion = System.currentTimeMillis() - 259_200_000L,
            analisisIA = AnalisisIA(
                tieneLenguajeInapropiado = false,
                palabrasDetectadas = emptyList(),
                versionSugerida = "",
                puntajeConfianza = 0.97f,
                recomendacion = "aprobar"
            )
        )
    )
}
