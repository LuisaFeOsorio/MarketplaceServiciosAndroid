package uniquindio.marketplace2.features.olvidar_contrasena

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uniquindio.marketplace2.features.olvidar_contrasenia.OlvidarContrasenaViewModel

@Composable
fun OlvidarContrasenaScreen(
    viewModel: OlvidarContrasenaViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Olvidé mi contraseña",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = viewModel.correo.value,
            onValueChange = { viewModel.cambiarCorreo(it) },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = viewModel.errorCorreo.value != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.errorCorreo.value != null) {
            Text(
                text = viewModel.errorCorreo.value!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.enviarCorreoRecuperacion() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar enlace")
        }

        if (viewModel.mensaje.value != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = viewModel.mensaje.value!!,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
