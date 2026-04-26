package uniquindio.marketplace2.features.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uniquindio.marketplace2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFeedMapa(
    onOfertaClick: (String) -> Unit,
    viewModel: FeedMapaViewModel = hiltViewModel()
) {
    val ofertas by viewModel.ofertas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val ubicacionActual by viewModel.ubicacionActual.collectAsState()
    var selectedOfertaId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.feed_mapa_titulo)) },
                actions = {
                    IconButton(onClick = { /* Centrar en ubicación */ }) {
                        Icon(Icons.Default.LocationOn, contentDescription = stringResource(R.string.feed_mi_ubicacion))
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
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // Placeholder del mapa (por ahora texto)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🗺️", fontSize = 48.sp)
                        Text(
                            text = stringResource(R.string.feed_mapa_titulo),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.feed_ubicacion_actual, ubicacionActual.nombre),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.feed_ofertas_disponibles, ofertas.size),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ofertas.take(3).forEach { oferta ->
                            Text(
                                text = "📍 ${oferta.titulo} - ${oferta.distanciaKm}${stringResource(R.string.feed_km)}",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            // Bottom sheet con detalle al seleccionar una oferta
            selectedOfertaId?.let { ofertaId ->
                val oferta = viewModel.obtenerOfertaPorId(ofertaId)
                if (oferta != null) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                oferta.titulo,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(oferta.proveedorNombre)
                            Row {
                                Text("$${oferta.precioMin} - $${oferta.precioMax}")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("📍 ${oferta.distanciaKm} ${stringResource(R.string.feed_km)}")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    onOfertaClick(oferta.id)
                                    selectedOfertaId = null
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(stringResource(R.string.feed_ver_detalle))
                            }
                        }
                    }
                }
            }
        }
    }
}