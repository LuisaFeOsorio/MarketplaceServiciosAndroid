package uniquindio.marketplace2.features.olvidar_contrasena

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import android.util.Patterns

class OlvidarContrasenaViewModel : ViewModel() {

    var correo = mutableStateOf("")
    var errorCorreo = mutableStateOf<String?>(null)
    var mensaje = mutableStateOf<String?>(null)

    fun cambiarCorreo(nuevoCorreo: String) {
        correo.value = nuevoCorreo
        errorCorreo.value = null
    }

    fun enviarCorreoRecuperacion() {

        if (correo.value.isEmpty()) {
            errorCorreo.value = "El correo es obligatorio"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo.value).matches()) {
            errorCorreo.value = "Correo no válido"
            return
        }

        mensaje.value = "Se envió un enlace de recuperación a tu correo"
    }
}
