package uniquindio.marketplace2.features.auth.recuperar_contrasenia

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
fun RecuperarContrasenaScreen(
    viewModel: RecuperarContrasenaViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val correo by viewModel.correo.collectAsState()
    val errorCorreo by viewModel.errorCorreo.collectAsState()
    val mensajeResultado by viewModel.mensajeResultado.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(mensajeResultado) {
        if (mensajeResultado != null) {
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
            text = stringResource(R.string.recuperar_titulo),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { viewModel.alCambiarCorreo(it) },
            label = { Text(stringResource(R.string.recuperar_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errorCorreo != null,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        if (errorCorreo != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorCorreo!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.recuperarContrasena() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(R.string.recuperar_enviar))
            }
        }

        if (mensajeResultado != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensajeResultado!!,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}