package uniquindio.marketplace2.features.recuperar_contrasena

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RecuperarContrasenaViewModel : ViewModel() {

    var correo = mutableStateOf("")
        private set

    var errorCorreo = mutableStateOf<String?>(null)
        private set

    var mensajeResultado = mutableStateOf<String?>(null)
        private set

    fun alCambiarCorreo(nuevoCorreo: String) {
        correo.value = nuevoCorreo
        validarCorreo()
    }

    private fun validarCorreo() {
        errorCorreo.value = when {
            correo.value.isBlank() -> "El correo no puede estar vacío"
            !correo.value.contains("@") -> "El correo no es válido"
            else -> null
        }
    }

    fun recuperarContrasena() {

        validarCorreo()

        if (errorCorreo.value != null) {
            mensajeResultado.value = null
            return
        }

        mensajeResultado.value =
            "Si el correo está registrado, se enviará un enlace para restablecer la contraseña."
    }
}
