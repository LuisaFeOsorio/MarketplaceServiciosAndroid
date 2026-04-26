package uniquindio.marketplace2.features.solicitudes.crear

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Message
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
import uniquindio.marketplace2.data.modelos.OfertasMock
import uniquindio.marketplace2.features.solicitudes.SolicitudViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaSolicitarServicio(
    ofertaId: String,
    onBackPressed: () -> Unit,
    onEnviarSolicitud: (String) -> Unit,
    viewModel: SolicitudViewModel = hiltViewModel()
) {
    val oferta = OfertasMock.lista.find { it.id == ofertaId }
    var mensaje by remember { mutableStateOf("") }
    var fechaSeleccionada by remember { mutableStateOf("") }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val isLoading by viewModel.isLoading.collectAsState()
    val solicitudEnviada by viewModel.solicitudEnviada.collectAsState()

    LaunchedEffect(solicitudEnviada) {
        if (solicitudEnviada) {
            onEnviarSolicitud(ofertaId)
            viewModel.resetSolicitudEnviada()
        }
    }

    if (oferta == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.solicitar_oferta_no_encontrada))
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.solicitar_titulo)) },
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
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = oferta.titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = oferta.proveedorNombre,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$${oferta.precioMin} - $${oferta.precioMax}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text(stringResource(R.string.solicitar_mensaje)) },
                leadingIcon = { Icon(Icons.Default.Message, null) },
                placeholder = { Text(stringResource(R.string.solicitar_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fechaSeleccionada,
                onValueChange = { },
                label = { Text(stringResource(R.string.solicitar_fecha)) },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                placeholder = { Text(stringResource(R.string.solicitar_fecha_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { mostrarDatePicker = true }) {
                        Text("📅")
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (mensaje.isNotBlank()) {
                        viewModel.enviarSolicitud(oferta.id, mensaje, fechaSeleccionada)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = mensaje.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(stringResource(R.string.solicitar_enviar), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (mostrarDatePicker) {
        MiDatePickerDialog(
            onDismiss = { mostrarDatePicker = false },
            onDateSelected = { fecha ->
                fechaSeleccionada = fecha
                mostrarDatePicker = false
            }
        )
    }
}

@Composable
fun MiDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val year = remember { Calendar.getInstance().get(Calendar.YEAR) }
    val month = remember { Calendar.getInstance().get(Calendar.MONTH) }
    val day = remember { Calendar.getInstance().get(Calendar.DAY_OF_MONTH) }

    var selectedYear by remember { mutableIntStateOf(year) }
    var selectedMonth by remember { mutableIntStateOf(month) }
    var selectedDay by remember { mutableIntStateOf(day) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.solicitar_fecha_selector_titulo)) },
        text = {
            Column {
                Text(stringResource(R.string.solicitar_fecha_anio), fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { if (selectedYear > 2000) selectedYear-- }) { Text("-") }
                    Text("$selectedYear", modifier = Modifier.padding(horizontal = 16.dp))
                    IconButton(onClick = { selectedYear++ }) { Text("+") }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(R.string.solicitar_fecha_mes), fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { if (selectedMonth > 0) selectedMonth-- }) { Text("-") }
                    Text(getMonthName(selectedMonth), modifier = Modifier.padding(horizontal = 16.dp))
                    IconButton(onClick = { if (selectedMonth < 11) selectedMonth++ }) { Text("+") }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(stringResource(R.string.solicitar_fecha_dia), fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { if (selectedDay > 1) selectedDay-- }) { Text("-") }
                    Text("$selectedDay", modifier = Modifier.padding(horizontal = 16.dp))
                    IconButton(onClick = { if (selectedDay < 31) selectedDay++ }) { Text("+") }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val fecha = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                    onDateSelected(fecha)
                }
            ) {
                Text(stringResource(R.string.aceptar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

fun getMonthName(month: Int): String {
    val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    return months.getOrElse(month) { "Enero" }
}