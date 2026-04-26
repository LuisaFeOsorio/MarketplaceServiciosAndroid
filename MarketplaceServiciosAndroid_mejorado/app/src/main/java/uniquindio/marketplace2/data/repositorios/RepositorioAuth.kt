package uniquindio.marketplace2.data.repositorios

import javax.inject.Inject
import javax.inject.Singleton
import uniquindio.marketplace2.data.modelos.Usuario

// ✅ CORREGIDO: Eliminado import de UsuariosMock que no se usaba en ningún método
@Singleton
class RepositorioAuth @Inject constructor() {

    fun login(email: String, password: String): Usuario? {
        return when {
            email == "cliente@test.com" && password == "123456" ->
                Usuario(
                    id = "1",
                    nombre = "Cliente Test",
                    email = email,
                    rol = "cliente"
                )
            email == "trabajador@test.com" && password == "123456" ->
                Usuario(
                    id = "2",
                    nombre = "Trabajador Test",
                    email = email,
                    rol = "trabajador"
                )
            email == "moderador@test.com" && password == "123456" ->
                Usuario(
                    id = "3",
                    nombre = "Moderador Test",
                    email = email,
                    rol = "moderador"
                )
            else -> null
        }
    }

    fun registrar(usuario: Usuario): Usuario? {
        return usuario.copy(id = System.currentTimeMillis().toString())
    }

    fun buscarPorEmail(email: String): Usuario? {
        return when (email) {
            "cliente@test.com" -> Usuario(
                id = "1",
                nombre = "Cliente Test",
                email = email,
                rol = "cliente"
            )
            "trabajador@test.com" -> Usuario(
                id = "2",
                nombre = "Trabajador Test",
                email = email,
                rol = "trabajador"
            )
            else -> null
        }
    }

    fun guardarSesionActiva(usuarioId: String) {
        println("Sesión guardada para usuario: $usuarioId")
    }

    fun obtenerUsuarioActual(): Usuario? {
        return null
    }

    fun cerrarSesion() {
        println("Sesión cerrada")
    }

    fun loginPruebaCliente(): Usuario {
        return Usuario(
            id = "1",
            nombre = "Cliente Test",
            email = "cliente@test.com",
            rol = "cliente"
        )
    }

    fun loginPruebaTrabajador(): Usuario {
        return Usuario(
            id = "2",
            nombre = "Trabajador Test",
            email = "trabajador@test.com",
            rol = "trabajador"
        )
    }

    fun loginPruebaModerador(): Usuario {
        return Usuario(
            id = "3",
            nombre = "Moderador Test",
            email = "moderador@test.com",
            rol = "moderador"
        )
    }

    fun registroPruebaCliente(): Usuario {
        return Usuario(
            id = System.currentTimeMillis().toString(),
            nombre = "Cliente Nuevo",
            email = "nuevo@test.com",
            rol = "cliente"
        )
    }

    fun registroPruebaTrabajador(): Usuario {
        return Usuario(
            id = System.currentTimeMillis().toString(),
            nombre = "Trabajador Nuevo",
            email = "trabajador@test.com",
            rol = "trabajador"
        )
    }
}
