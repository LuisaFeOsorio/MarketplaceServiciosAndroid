package uniquindio.marketplace2.features.recuperar_contrasena

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RecuperarContrasenaScreen(
    viewModel: RecuperarContrasenaViewModel = viewModel()
) {

    val correo = viewModel.correo.value
    val errorCorreo = viewModel.errorCorreo.value
    val mensajeResultado = viewModel.mensajeResultado.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { viewModel.alCambiarCorreo(it) },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errorCorreo != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorCorreo != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorCorreo,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.recuperarContrasena() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar enlace")
        }

        if (mensajeResultado != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensajeResultado,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
