package uniquindio.marketplace2.features.ofertas.crear

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uniquindio.marketplace2.R
import uniquindio.marketplace2.data.model.Oferta
import uniquindio.marketplace2.data.modelos.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearOferta(
    usuario: Usuario,
    onBackPressed: () -> Unit,
    onOfertaCreada: (Oferta) -> Unit,
    viewModel: CrearOfertaViewModel = hiltViewModel()
) {
    val titulo by viewModel.titulo.collectAsState()
    val categoria by viewModel.categoria.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val precioMin by viewModel.precioMin.collectAsState()
    val precioMax by viewModel.precioMax.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val ofertaCreada by viewModel.ofertaCreada.collectAsState()

    val scrollState = rememberScrollState()
    val categorias = viewModel.categorias

    LaunchedEffect(ofertaCreada) {
        ofertaCreada?.let {
            onOfertaCreada(it)
            viewModel.resetOfertaCreada()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.crear_oferta_titulo)) },
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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Información del proveedor
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(stringResource(R.string.crear_oferta_publicando), fontSize = 12.sp)
                        Text(
                            usuario.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título
            OutlinedTextField(
                value = titulo,
                onValueChange = viewModel::onTituloChange,
                label = { Text(stringResource(R.string.crear_oferta_titulo_label)) },
                placeholder = { Text(stringResource(R.string.crear_oferta_titulo_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Title, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Categoría - Dropdown manual
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                TextField(
                    value = categoria,
                    onValueChange = {},
                    label = { Text(stringResource(R.string.crear_oferta_categoria)) },
                    leadingIcon = { Icon(Icons.Default.Category, null) },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    when (cat) {
                                        "Hogar" -> stringResource(R.string.feed_categoria_hogar)
                                        "Educación" -> stringResource(R.string.feed_categoria_educacion)
                                        "Mascotas" -> stringResource(R.string.feed_categoria_mascotas)
                                        "Tecnología" -> stringResource(R.string.feed_categoria_tecnologia)
                                        "Transporte" -> stringResource(R.string.feed_categoria_transporte)
                                        else -> cat
                                    }
                                )
                            },
                            onClick = {
                                viewModel.onCategoriaChange(cat)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = { Text(stringResource(R.string.crear_oferta_descripcion)) },
                placeholder = { Text(stringResource(R.string.crear_oferta_descripcion_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Description, null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 8
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Precios
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = precioMin,
                    onValueChange = viewModel::onPrecioMinChange,
                    label = { Text(stringResource(R.string.crear_oferta_precio_min)) },
                    leadingIcon = { Text("$", fontSize = 18.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = precioMax,
                    onValueChange = viewModel::onPrecioMaxChange,
                    label = { Text(stringResource(R.string.crear_oferta_precio_max)) },
                    leadingIcon = { Text("$", fontSize = 18.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.crear_oferta_rango),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Mensaje de error
            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón publicar
            Button(
                onClick = {
                    viewModel.publicarOferta(
                        proveedorId = usuario.id,
                        proveedorNombre = usuario.nombre
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.crear_oferta_publicar), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}