package uniquindio.marketplace2.features.moderador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import uniquindio.marketplace2.R
import uniquindio.marketplace2.features.moderador.componentes.DialogoDetalleOferta
import uniquindio.marketplace2.features.moderador.componentes.DialogoMotivoRechazo
import uniquindio.marketplace2.features.moderador.componentes.TarjetaOfertaPendiente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPanelModerador(
    onBackPressed: () -> Unit,
    viewModel: ModeradorViewModel = hiltViewModel()
) {
    val ofertasPendientes     by viewModel.ofertasPendientes.collectAsState()
    val isLoading             by viewModel.isLoading.collectAsState()
    val ofertaSeleccionada    by viewModel.ofertaSeleccionada.collectAsState()
    val accionExitosa         by viewModel.accionExitosa.collectAsState()
    val mostrarDialogoRechazo by viewModel.mostrarDialogoRechazo.collectAsState()
    val motivoRechazo         by viewModel.motivoRechazo.collectAsState()
    val error                 by viewModel.error.collectAsState()

    // Los helpers de Color viven en la capa UI, no en el ViewModel
    val getColor  = ModeradorUiHelper::getColorRecomendacion
    val getTexto  = ModeradorUiHelper::getTextoRecomendacion

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.moderador_panel_titulo)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.volver))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                ofertasPendientes.isEmpty() -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("✅", fontSize = 48.sp)
                    Text(
                        stringResource(R.string.moderador_sin_pendientes),
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        stringResource(R.string.moderador_todas_revisadas),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = ofertasPendientes,
                        key = { it.id }
                    ) { oferta ->
                        TarjetaOfertaPendiente(
                            oferta = oferta,
                            onClick = { viewModel.seleccionarOferta(oferta) },
                            getColorRecomendacion = getColor,
                            getTextoRecomendacion = getTexto
                        )
                    }
                }
            }

            // Snackbar de confirmación
            if (accionExitosa) {
                LaunchedEffect(accionExitosa) {
                    delay(2000)
                    viewModel.resetAccionExitosa()
                }
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.moderador_accion_exitosa),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }

    // Diálogos fuera del Scaffold
    ofertaSeleccionada?.let { oferta ->
        DialogoDetalleOferta(
            oferta = oferta,
            onDismiss = { viewModel.limpiarSeleccion() },
            onAprobar = { viewModel.aprobarOferta(oferta.id) },
            onRechazar = { viewModel.abrirDialogoRechazo() },
            getColorRecomendacion = getColor,
            getTextoRecomendacion = getTexto
        )
    }

    if (mostrarDialogoRechazo) {
        DialogoMotivoRechazo(
            motivo = motivoRechazo,
            onMotivoChange = viewModel::onMotivoRechazoChange,
            onConfirmar = {
                ofertaSeleccionada?.let { oferta ->
                    viewModel.rechazarOferta(oferta.id)
                }
            },
            onDismiss = viewModel::cerrarDialogoRechazo,
            isLoading = isLoading,
            error = error
        )
    }
}
