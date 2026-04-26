package uniquindio.marketplace2.features.auth.olvidar_contrasenia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uniquindio.marketplace2.R

@Composable
fun OlvidarContrasenaScreen(
    viewModel: OlvidarContrasenaViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val correo by viewModel.correo.collectAsState()
    val errorCorreo by viewModel.errorCorreo.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            kotlinx.coroutines.delay(2000)
            viewModel.limpiarMensaje()
            onNavigateToLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.olvidar_titulo),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { viewModel.cambiarCorreo(it) },
            label = { Text(stringResource(R.string.olvidar_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errorCorreo != null,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        if (errorCorreo != null) {
            Text(
                text = errorCorreo!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.enviarCorreoRecuperacion() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(R.string.olvidar_enviar))
            }
        }

        if (mensaje != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensaje!!,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}