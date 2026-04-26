package uniquindio.marketplace2.data.modelos

data class Oferta(
    val id: String,
    val titulo: String,
    val categoria: String,
    val descripcion: String,
    val precioMin: Double,
    val precioMax: Double,
    val proveedorId: String,
    val proveedorNombre: String,
    val distanciaKm: Double,
    val imagenUrl: String? = null,
    val calificacion: Float = 0f,
    val totalResenas: Int = 0,
    val estado: String = "activa",
    val etiquetas: List<String> = emptyList(),
    val disponible: Boolean = true,

    val votos: Int = 0,
    val usuariosQueVotaron: List<String> = emptyList(),
    val comentarios: List<Comentario> = emptyList()
)

data class Comentario(
    val id: String,
    val autorId: String,
    val autorNombre: String,
    val texto: String,
    val fecha: String
)

object OfertasMock {
    val lista = mutableListOf(
        Oferta(
            id = "1", titulo = "Reparación de Fugas 24/7", categoria = "Hogar",
            descripcion = "Plomero especializado en detección y reparación de fugas de agua y gas. Atención de emergencias a cualquier hora del día. Garantía de 6 meses en todos mis trabajos.",
            precioMin = 40.0, precioMax = 80.0, proveedorId = "2", proveedorNombre = "Juan Pérez",
            distanciaKm = 2.5, calificacion = 4.8f, totalResenas = 47,
            etiquetas = listOf("urgencias", "garantía", "24/7"),
            votos = 12,
            comentarios = listOf(
                Comentario("c1", "3", "Pedro Ruiz", "¿Trabajan los fines de semana?", "20/03/2024"),
                Comentario("c2", "2", "Juan Pérez", "Sí, estamos disponibles 24/7 incluyendo festivos.", "20/03/2024")
            )
        ),
        Oferta(
            id = "2", titulo = "Clases de Matemáticas y Física", categoria = "Educación",
            descripcion = "Profesor universitario con 10 años de experiencia en clases particulares de matemáticas, física y cálculo. Metodología adaptada a cada estudiante.",
            precioMin = 25.0, precioMax = 50.0, proveedorId = "4", proveedorNombre = "María González",
            distanciaKm = 1.2, calificacion = 4.9f, totalResenas = 89,
            etiquetas = listOf("universitario", "bachillerato", "admisión"),
            votos = 23
        ),
        Oferta(
            id = "3", titulo = "Paseo y Cuidado de Mascotas", categoria = "Mascotas",
            descripcion = "Servicio de paseo responsable para perros. Incluye reporte con fotos del paseo, hidratación y limpieza de patas al regresar. Experiencia con todas las razas.",
            precioMin = 15.0, precioMax = 30.0, proveedorId = "5", proveedorNombre = "Carlos Ruiz",
            distanciaKm = 3.1, calificacion = 4.5f, totalResenas = 32,
            etiquetas = listOf("perros", "gatos", "fotos"),
            votos = 5
        ),
        Oferta(
            id = "4", titulo = "Instalaciones Eléctricas Residenciales", categoria = "Hogar",
            descripcion = "Electricista certificado. Instalación de tomacorrientes, interruptores, breakers, lámparas y sistemas eléctricos completos. Certificado RETIE. Trabajo con garantía.",
            precioMin = 60.0, precioMax = 150.0, proveedorId = "6", proveedorNombre = "Roberto Silva",
            distanciaKm = 1.8, calificacion = 4.7f, totalResenas = 63,
            etiquetas = listOf("RETIE", "residencial", "garantía"),
            votos = 8
        ),
        Oferta(
            id = "5", titulo = "Soporte Técnico Computadores", categoria = "Tecnología",
            descripcion = "Técnico en sistemas con 8 años de experiencia. Formateo, instalación de software, reparación de hardware, eliminación de virus, configuración de redes WiFi.",
            precioMin = 30.0, precioMax = 80.0, proveedorId = "7", proveedorNombre = "Ana Martínez",
            distanciaKm = 0.8, calificacion = 4.6f, totalResenas = 28,
            etiquetas = listOf("PC", "laptop", "redes"),
            votos = 4
        ),
        Oferta(
            id = "6", titulo = "Servicio de Domicilios Express", categoria = "Transporte",
            descripcion = "Mensajería y domicilios en moto. Entrega de documentos, paquetes pequeños y diligencias. Cobertura toda la ciudad. Tarifa especial para empresas.",
            precioMin = 8.0, precioMax = 20.0, proveedorId = "8", proveedorNombre = "Diego López",
            distanciaKm = 4.2, calificacion = 4.3f, totalResenas = 156,
            etiquetas = listOf("express", "empresas", "paquetes"),
            votos = 31
        ),
        Oferta(
            id = "7", titulo = "Clases de Inglés Conversacional", categoria = "Educación",
            descripcion = "Profesor nativo bilingüe. Clases virtuales y presenciales. Nivel A1 a C2. Preparación para exámenes IELTS, TOEFL y Cambridge. Metodología comunicativa.",
            precioMin = 35.0, precioMax = 70.0, proveedorId = "9", proveedorNombre = "Laura Ramírez",
            distanciaKm = 2.0, calificacion = 5.0f, totalResenas = 41,
            etiquetas = listOf("IELTS", "TOEFL", "virtual"),
            votos = 17
        ),
        Oferta(
            id = "8", titulo = "Pintura de Interiores y Exteriores", categoria = "Hogar",
            descripcion = "Maestro pintor con 15 años de experiencia. Pintura en latex, esmalte, estuco y efectos decorativos. Presupuesto sin costo. Trabajo limpio y terminados perfectos.",
            precioMin = 80.0, precioMax = 200.0, proveedorId = "10", proveedorNombre = "Fernando Castro",
            distanciaKm = 5.0, calificacion = 4.4f, totalResenas = 22,
            etiquetas = listOf("latex", "estuco", "presupuesto gratis"),
            votos = 3
        ),
        Oferta(
            id = "9", titulo = "Diseño Web y Apps Móviles", categoria = "Tecnología",
            descripcion = "Desarrollador full-stack con experiencia en React, Flutter y Django. Desarrollo de páginas web empresariales, e-commerce y aplicaciones móviles iOS y Android.",
            precioMin = 200.0, precioMax = 800.0, proveedorId = "11", proveedorNombre = "Santiago Vargas",
            distanciaKm = 1.5, calificacion = 4.9f, totalResenas = 15,
            etiquetas = listOf("React", "Flutter", "e-commerce"),
            votos = 9
        ),
        Oferta(
            id = "10", titulo = "Mudanzas y Trasteos", categoria = "Transporte",
            descripcion = "Servicio completo de mudanzas. Embalaje, cargue, transporte y descargue. Camión de 5 toneladas disponible. Personal capacitado. Manejo cuidadoso de muebles.",
            precioMin = 150.0, precioMax = 400.0, proveedorId = "12", proveedorNombre = "Transportes García",
            distanciaKm = 6.3, calificacion = 4.2f, totalResenas = 78,
            etiquetas = listOf("embalaje", "camión", "electrodomésticos"),
            votos = 6
        ),
        Oferta(
            id = "11", titulo = "Veterinaria a Domicilio", categoria = "Mascotas",
            descripcion = "Veterinaria graduada. Consulta, vacunación, desparasitación y cirugías menores en la comodidad de tu hogar. Atención especializada para perros y gatos.",
            precioMin = 50.0, precioMax = 120.0, proveedorId = "13", proveedorNombre = "Dra. Patricia Mora",
            distanciaKm = 3.7, calificacion = 4.8f, totalResenas = 55,
            etiquetas = listOf("vacunación", "cirugía", "domicilio"),
            votos = 14
        ),
        Oferta(
            id = "12", titulo = "Fotografía Profesional", categoria = "Tecnología",
            descripcion = "Fotógrafo profesional para eventos, retratos, productos y fotos corporativas. Edición incluida. Entrega digital en menos de 72 horas. Sesiones indoor y outdoor.",
            precioMin = 100.0, precioMax = 350.0, proveedorId = "14", proveedorNombre = "Camila Torres",
            distanciaKm = 2.9, calificacion = 4.7f, totalResenas = 33,
            etiquetas = listOf("eventos", "corporativo", "edición"),
            votos = 7
        )
    )

    val misOfertas get() = lista.filter { it.proveedorId == "2" }
}

data class Resena(
    val id: String,
    val autorNombre: String,
    val calificacion: Float,
    val comentario: String,
    val fecha: String
)

object ResenasMock {
    val lista = mapOf(
        "1" to listOf(
            Resena("r1", "Carlos M.", 5.0f, "Excelente servicio, llegó a tiempo y resolvió el problema rápidamente.", "15/03/2024"),
            Resena("r2", "Ana P.", 4.5f, "Muy profesional, dejó todo limpio. Recomendado.", "12/03/2024"),
            Resena("r3", "Pedro L.", 5.0f, "Solucionó una fuga complicada en menos de una hora. Increíble.", "08/03/2024")
        ),
        "2" to listOf(
            Resena("r4", "Sofía R.", 5.0f, "Mis hijos mejoraron muchísimo en matemáticas. Excelente metodología.", "14/03/2024"),
            Resena("r5", "José M.", 4.8f, "Muy paciente y explica de forma muy clara. Totalmente recomendado.", "10/03/2024")
        )
    )
    fun obtenerPorOferta(ofertaId: String) = lista[ofertaId] ?: emptyList()
}
