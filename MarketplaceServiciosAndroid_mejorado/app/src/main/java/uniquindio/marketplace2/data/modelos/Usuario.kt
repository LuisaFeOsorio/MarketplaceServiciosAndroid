package uniquindio.marketplace2.data.modelos

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val rol: String,
    val telefono: String? = null,
    val fotoPerfil: String? = null,
    val descripcionProfesional: String? = null,
    val calificacionPromedio: Float = 0f,
    val serviciosCompletados: Int = 0
)

// Datos mock
object UsuariosMock {
    val clienteEjemplo = Usuario(
        id = "1",
        nombre = "María García",
        email = "cliente@test.com",
        rol = "cliente",
        telefono = "3001234567"
    )

    val trabajadorEjemplo = Usuario(
        id = "2",
        nombre = "Juan Pérez",
        email = "trabajador@test.com",
        rol = "trabajador",
        telefono = "3007654321",
        descripcionProfesional = "Electricista con 5 años de experiencia",
        calificacionPromedio = 4.8f,
        serviciosCompletados = 18
    )
}