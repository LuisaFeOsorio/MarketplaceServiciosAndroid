package uniquindio.marketplace2.features.solicitudes.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uniquindio.marketplace2.R
import uniquindio.marketplace2.data.modelos.Solicitud
import uniquindio.marketplace2.features.solicitudes.SolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaSolicitudes(
    // ✅ CORREGIDO: rolUsuario ya no se pasa como parámetro hardcodeado.
    //    El ViewModel lo obtiene de la sesión activa.
    onBackPressed: () -> Unit,
    onSolicitudClick: (String) -> Unit,
    onAceptarSolicitud: (String) -> Unit,
    onRechazarSolicitud: (String) -> Unit,
    viewModel: SolicitudViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.solicitudes_pendientes),
        stringResource(R.string.solicitudes_aceptadas),
        stringResource(R.string.solicitudes_completadas)
    )

    val solicitudes by viewModel.solicitudes.collectAsState()
    val rolActual by viewModel.rolUsuario.collectAsState()

    // ✅ CORREGIDO: Carga usando la sesión real en vez de IDs hardcodeados
    LaunchedEffect(Unit) {
        viewModel.cargarSolicitudesSegunRol()
    }

    val solicitudesFiltradas = when (selectedTab) {
        0 -> solicitudes.filter { it.estado == "pendiente" }
        1 -> solicitudes.filter { it.estado == "aceptada" }
        else -> solicitudes.filter { it.estado == "completada" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (rolActual == "trabajador") stringResource(R.string.solicitudes_recibidas)
                        else stringResource(R.string.mis_solicitudes)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.volver))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, modifier = Modifier.padding(vertical = 12.dp)) }
                    )
                }
            }

            if (solicitudesFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📭", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.solicitudes_vacio, tabs[selectedTab].lowercase()))
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(solicitudesFiltradas) { solicitud ->
                        TarjetaSolicitud(
                            solicitud = solicitud,
                            rolUsuario = rolActual,
                            onClick = { onSolicitudClick(solicitud.id) },
                            onAceptar = { onAceptarSolicitud(solicitud.id) },
                            onRechazar = { onRechazarSolicitud(solicitud.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaSolicitud(
    solicitud: Solicitud,
    rolUsuario: String,
    onClick: () -> Unit,
    onAceptar: () -> Unit,
    onRechazar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(solicitud.clienteNombre, fontWeight = FontWeight.Bold)
                }

                when (solicitud.estado) {
                    "pendiente" -> Surface(color = Color(0xFFFFA726), shape = RoundedCornerShape(12.dp)) {
                        Text(stringResource(R.string.solicitud_estado_pendiente), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                    "aceptada" -> Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp)) {
                        Text(stringResource(R.string.solicitud_estado_aceptada), fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                    "completada" -> Surface(color = Color(0xFF9E9E9E), shape = RoundedCornerShape(12.dp)) {
                        Text(stringResource(R.string.solicitud_estado_completada), fontSize = 10.sp, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }
            }

            Text(solicitud.ofertaTitulo, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Icon(Icons.Default.Schedule, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.solicitud_para, solicitud.fechaPreferida), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Text(solicitud.mensaje, fontSize = 12.sp, maxLines = 2, modifier = Modifier.padding(top = 8.dp))

            if (rolUsuario == "trabajador" && solicitud.estado == "pendiente") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onAceptar, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.boton_aceptar))
                    }
                    OutlinedButton(onClick = onRechazar, modifier = Modifier.weight(1f), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF44336))) {
                        Text(stringResource(R.string.boton_rechazar))
                    }
                }
            }
        }
    }
}
