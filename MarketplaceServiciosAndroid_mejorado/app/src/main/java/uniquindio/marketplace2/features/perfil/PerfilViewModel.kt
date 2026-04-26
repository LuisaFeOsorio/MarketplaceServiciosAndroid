package uniquindio.marketplace2.features.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uniquindio.marketplace2.core.datastore.SessionManager
import uniquindio.marketplace2.data.modelos.Usuario
import uniquindio.marketplace2.data.repositorios.RepositorioAuth
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val repositorioAuth: RepositorioAuth
) : ViewModel() {

    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _nombreEdit = MutableStateFlow("")
    val nombreEdit: StateFlow<String> = _nombreEdit.asStateFlow()

    private val _telefonoEdit = MutableStateFlow("")
    val telefonoEdit: StateFlow<String> = _telefonoEdit.asStateFlow()

    private val _descripcionEdit = MutableStateFlow("")
    val descripcionEdit: StateFlow<String> = _descripcionEdit.asStateFlow()

    init { cargarPerfil() }

    fun cargarPerfil() {
        viewModelScope.launch {
            _isLoading.value = true
            // ✅ CORREGIDO: Carga el usuario REAL de la sesión activa
            val sesion = sessionManager.getUserInfo().first()
            if (sesion != null) {
                val usuario = Usuario(
                    id = sesion.userId,
                    nombre = sesion.nombre,
                    email = sesion.email,
                    rol = sesion.rol
                )
                _usuario.value = usuario
                _nombreEdit.value = usuario.nombre
                _telefonoEdit.value = usuario.telefono ?: ""
                _descripcionEdit.value = usuario.descripcionProfesional ?: ""
            } else {
                _error.value = "No se pudo cargar el perfil"
            }
            _isLoading.value = false
        }
    }

    fun iniciarEdicion() { _isEditing.value = true }
    fun cancelarEdicion() {
        _isEditing.value = false
        _nombreEdit.value = _usuario.value?.nombre ?: ""
        _telefonoEdit.value = _usuario.value?.telefono ?: ""
        _descripcionEdit.value = _usuario.value?.descripcionProfesional ?: ""
        _error.value = null
    }

    fun onNombreEditChange(v: String) { _nombreEdit.value = v }
    fun onTelefonoEditChange(v: String) { _telefonoEdit.value = v }
    fun onDescripcionEditChange(v: String) { _descripcionEdit.value = v }

    fun guardarCambios() {
        if (_nombreEdit.value.isBlank()) { _error.value = "El nombre no puede estar vacío"; return }
        viewModelScope.launch {
            _isLoading.value = true
            val usuarioActualizado = _usuario.value?.copy(
                nombre = _nombreEdit.value.trim(),
                telefono = _telefonoEdit.value.trim(),
                descripcionProfesional = _descripcionEdit.value.trim()
            )
            _usuario.value = usuarioActualizado
            // ✅ Actualizar también la sesión guardada con el nuevo nombre
            usuarioActualizado?.let {
                sessionManager.saveSession(it.id, it.nombre, it.email, it.rol)
            }
            _isEditing.value = false
            _isLoading.value = false
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch { sessionManager.clearSession() }
    }

    fun limpiarError() { _error.value = null }
}
