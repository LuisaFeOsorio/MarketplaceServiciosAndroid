package uniquindio.marketplace2.data.repositorios

import javax.inject.Inject
import javax.inject.Singleton

import uniquindio.marketplace2.data.model.Oferta
import uniquindio.marketplace2.data.model.OfertasMock

@Singleton
class RepositorioOfertas @Inject constructor() {

    // Lista mutable en memoria (simula base de datos)
    private val ofertas = OfertasMock.lista.toMutableList()

    fun obtenerTodas(): List<Oferta> = ofertas.toList()

    fun obtenerActivas(): List<Oferta> = ofertas.filter { it.estado == "activa" }

    fun obtenerPorCategoria(categoria: String): List<Oferta> =
        if (categoria == "Todos") obtenerActivas()
        else ofertas.filter { it.categoria == categoria && it.estado == "activa" }

    fun obtenerPorProveedor(proveedorId: String): List<Oferta> =
        ofertas.filter { it.proveedorId == proveedorId }

    fun obtenerPorId(id: String): Oferta? = ofertas.find { it.id == id }

    fun crear(oferta: Oferta): Oferta {
        val nueva = oferta.copy(
            id = System.currentTimeMillis().toString(),
            estado = "pendiente"
        )
        ofertas.add(nueva)
        return nueva
    }

    fun actualizar(oferta: Oferta): Boolean {
        val index = ofertas.indexOfFirst { it.id == oferta.id }
        return if (index >= 0) {
            ofertas[index] = oferta
            true
        } else false
    }

    fun cambiarEstado(id: String, nuevoEstado: String): Boolean {
        val index = ofertas.indexOfFirst { it.id == id }
        return if (index >= 0) {
            ofertas[index] = ofertas[index].copy(estado = nuevoEstado)
            true
        } else false
    }

    fun eliminar(id: String): Boolean {
        return ofertas.removeIf { it.id == id }
    }

    fun buscar(query: String): List<Oferta> =
        ofertas.filter { oferta ->
            oferta.titulo.contains(query, ignoreCase = true) ||
            oferta.descripcion.contains(query, ignoreCase = true) ||
            oferta.categoria.contains(query, ignoreCase = true) ||
            oferta.proveedorNombre.contains(query, ignoreCase = true)
        }
}
