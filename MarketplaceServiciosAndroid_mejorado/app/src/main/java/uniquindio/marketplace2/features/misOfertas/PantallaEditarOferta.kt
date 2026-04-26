package uniquindio.marketplace2.features.misOfertas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.marketplace2.data.modelos.Oferta
import uniquindio.marketplace2.data.repositorios.RepositorioOfertas


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarOferta(
    ofertaId: String,
    repositorioOfertas: RepositorioOfertas,
    onBackPressed: () -> Unit,
    onOfertaActualizada: (Oferta) -> Unit
) {
    val ofertaOriginal = remember(ofertaId) { repositorioOfertas.obtenerPorId(ofertaId) }

    if (ofertaOriginal == null) {
        LaunchedEffect(Unit) { onBackPressed() }
        return
    }

    var titulo      by remember { mutableStateOf(ofertaOriginal.titulo) }
    var descripcion by remember { mutableStateOf(ofertaOriginal.descripcion) }
    var precioMin   by remember { mutableStateOf(ofertaOriginal.precioMin.toString()) }
    var precioMax   by remember { mutableStateOf(ofertaOriginal.precioMax.toString()) }
    var categoriaSeleccionada by remember { mutableStateOf(ofertaOriginal.categoria) }
    var expandidoCategoria by remember { mutableStateOf(false) }

    val categorias = listOf("Hogar", "Educación", "Mascotas", "Tecnología", "Transporte", "Otro")

    var errorMsg by remember { mutableStateOf<String?>(null) }
    var guardando by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    fun guardar() {
        if (titulo.isBlank()) { errorMsg = "El título no puede estar vacío"; return }
        val pMin = precioMin.toDoubleOrNull()
        val pMax = precioMax.toDoubleOrNull()
        if (pMin == null || pMax == null || pMin < 0 || pMax < pMin) {
            errorMsg = "Verifica el rango de precios"; return
        }
        guardando = true
        val actualizada = ofertaOriginal.copy(
            titulo      = titulo.trim(),
            descripcion = descripcion.trim(),
            categoria   = categoriaSeleccionada,
            precioMin   = pMin,
            precioMax   = pMax
        )
        repositorioOfertas.actualizar(actualizada)
        onOfertaActualizada(actualizada)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar oferta") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = ::guardar, enabled = !guardando) {
                        Text("Guardar", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (ofertaOriginal.estado == "pendiente") {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.tertiary)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Esta oferta está en revisión. Al guardar cambios volverá a revisión.",
                            fontSize = 13.sp
                        )
                    }
                }
            }
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del servicio") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Title, null) }
            )

            // ── Categoría (dropdown) ───────────────────────────
            ExposedDropdownMenuBox(
                expanded = expandidoCategoria,
                onExpandedChange = { expandidoCategoria = !expandidoCategoria }
            ) {
                OutlinedTextField(
                    value = categoriaSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCategoria) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Category, null) }
                )
                ExposedDropdownMenu(
                    expanded = expandidoCategoria,
                    onDismissRequest = { expandidoCategoria = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoriaSeleccionada = cat
                                expandidoCategoria = false
                            }
                        )
                    }
                }
            }

            // ── Descripción ────────────────────────────────────
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                maxLines = 6,
                leadingIcon = { Icon(Icons.Default.Description, null) }
            )

            // ── Precios ────────────────────────────────────────
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = precioMin,
                    onValueChange = { precioMin = it },
                    label = { Text("Precio mín.") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text("$") }
                )
                OutlinedTextField(
                    value = precioMax,
                    onValueChange = { precioMax = it },
                    label = { Text("Precio máx.") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text("$") }
                )
            }

            // ── Error ──────────────────────────────────────────
            errorMsg?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.width(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 13.sp)
                    }
                }
            }

            // ── Botón guardar ──────────────────────────────────
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = ::guardar,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = !guardando
            ) {
                if (guardando) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Icon(Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar cambios", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
