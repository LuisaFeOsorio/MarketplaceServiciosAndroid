package uniquindio.marketplace2.features.ofertas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uniquindio.marketplace2.R
import uniquindio.marketplace2.data.modelos.Comentario
import uniquindio.marketplace2.data.modelos.ResenasMock

// ════════════════════════════════════════════════════════════
//  CAMBIOS RESPECTO AL ORIGINAL:
//  • Botón "Me interesa / Es importante" con contador de votos
//  • Sección de Comentarios con campo para agregar nuevos
//  • SnackbarHost para confirmación de votos
// ════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleOferta(
    ofertaId: String,
    onBackPressed: () -> Unit,
    onSolicitarClick: (String) -> Unit,
    viewModel: DetalleOfertaViewModel = hiltViewModel()
) {
    val oferta          by viewModel.oferta.collectAsState()
    val isLoading       by viewModel.isLoading.collectAsState()
    val error           by viewModel.error.collectAsState()
    val haVotado        by viewModel.haVotado.collectAsState()
    val mensajeVoto     by viewModel.mensajeVoto.collectAsState()
    val comentarios     by viewModel.comentarios.collectAsState()
    val textoComentario by viewModel.textoComentario.collectAsState()
    val enviando        by viewModel.enviandoComentario.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(ofertaId) { viewModel.cargarOferta(ofertaId) }

    LaunchedEffect(mensajeVoto) {
        mensajeVoto?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.limpiarMensajeVoto()
        }
    }

    // ── Estados de carga/error ────────────────────────────────
    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }
        error != null -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(R.string.detalle_titulo)) },
                        navigationIcon = {
                            IconButton(onClick = onBackPressed) {
                                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.volver))
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    )
                }
            ) { padding ->
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("😕", fontSize = 48.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(error ?: "Oferta no disponible", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = onBackPressed) { Text(stringResource(R.string.volver)) }
                    }
                }
            }
            return
        }
        oferta == null -> return
    }

    val ofertaActual = oferta!!

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(ofertaActual.titulo, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.volver))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Imagen ──────────────────────────────────────
            item {
                AsyncImage(
                    model = ofertaActual.imagenUrl,
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(R.string.detalle_imagen_servicio),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            // ── Título + categoría ───────────────────────────
            item {
                Column {
                    Text(ofertaActual.titulo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Row {
                        Text(
                            text = when (ofertaActual.categoria) {
                                "Hogar"      -> stringResource(R.string.feed_categoria_hogar)
                                "Educación"  -> stringResource(R.string.feed_categoria_educacion)
                                "Mascotas"   -> stringResource(R.string.feed_categoria_mascotas)
                                "Tecnología" -> stringResource(R.string.feed_categoria_tecnologia)
                                "Transporte" -> stringResource(R.string.feed_categoria_transporte)
                                else         -> ofertaActual.categoria
                            },
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // ── NUEVO: Botón "Me interesa / Es importante" ──
            item {
                BotonMeInteresa(
                    votos    = ofertaActual.votos,
                    haVotado = haVotado,
                    onClick  = { viewModel.toggleVoto() }
                )
            }

            // ── Precio ──────────────────────────────────────
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(stringResource(R.string.detalle_rango_estimado), fontSize = 12.sp)
                        Text(
                            text = stringResource(R.string.feed_precio_formato, ofertaActual.precioMin, ofertaActual.precioMax),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // ── Proveedor ────────────────────────────────────
            item {
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(ofertaActual.proveedorNombre, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                Text(stringResource(R.string.feed_calificacion_formato, ofertaActual.calificacion), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // ── Descripción ──────────────────────────────────
            item {
                Column {
                    Text(stringResource(R.string.detalle_descripcion), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(ofertaActual.descripcion)
                }
            }

            // ── Distancia ────────────────────────────────────
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍", fontSize = 18.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.detalle_distancia, ofertaActual.distanciaKm))
                }
            }

            // ── Etiquetas ────────────────────────────────────
            if (ofertaActual.etiquetas.isNotEmpty()) {
                item {
                    Column {
                        Text("Etiquetas", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ofertaActual.etiquetas.forEach { tag ->
                                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(12.dp)) {
                                    Text(
                                        "#$tag", fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Reseñas (originales) ─────────────────────────
            item {
                val resenas = ResenasMock.obtenerPorOferta(ofertaActual.id)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Reseñas (${resenas.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFCD34D), modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(2.dp))
                            Text("%.1f".format(ofertaActual.calificacion), fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    if (resenas.isEmpty()) {
                        Text("Aún no hay reseñas.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        resenas.forEach { resena ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = CardDefaults.cardElevation(1.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(resena.autorNombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                        Row {
                                            repeat(resena.calificacion.toInt()) {
                                                Icon(Icons.Default.Star, null, tint = Color(0xFFFCD34D), modifier = Modifier.size(12.dp))
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text(resena.comentario, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(Modifier.height(4.dp))
                                    Text(resena.fecha, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }

            // ── NUEVO: Sección de Comentarios ─────────────────
            item {
                SeccionComentarios(
                    comentarios     = comentarios,
                    textoNuevo      = textoComentario,
                    onTextoChange   = viewModel::onTextoComentarioChange,
                    onPublicar      = viewModel::publicarComentario,
                    enviando        = enviando
                )
            }

            // ── Botón solicitar ──────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onSolicitarClick(ofertaActual.id) },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(
                        text = stringResource(R.string.detalle_solicitar),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
//  Componente: Botón "Me interesa / Es importante"
// ════════════════════════════════════════════════════════════
@Composable
private fun BotonMeInteresa(
    votos: Int,
    haVotado: Boolean,
    onClick: () -> Unit
) {
    val colorBoton = if (haVotado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val colorFondo = if (haVotado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colorFondo,
            contentColor   = colorBoton
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            // Reutilizamos el border existente; el color cambia vía contentColor
        )
    ) {
        Icon(
            imageVector = if (haVotado) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = colorBoton,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = if (haVotado) "¡Me interesa! ($votos)" else "Me interesa ($votos)",
            fontWeight = if (haVotado) FontWeight.Bold else FontWeight.Normal,
            color = colorBoton
        )
    }
}

// ════════════════════════════════════════════════════════════
//  Componente: Sección de comentarios + campo de entrada
// ════════════════════════════════════════════════════════════
@Composable
private fun SeccionComentarios(
    comentarios: List<Comentario>,
    textoNuevo: String,
    onTextoChange: (String) -> Unit,
    onPublicar: () -> Unit,
    enviando: Boolean
) {
    Column {
        Text("Comentarios (${comentarios.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        // Lista de comentarios existentes
        if (comentarios.isEmpty()) {
            Text(
                "Sé el primero en comentar esta oferta.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            comentarios.forEach { cmt ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(Modifier.padding(10.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(cmt.autorNombre, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(cmt.fecha, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(cmt.texto, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Campo para nuevo comentario
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = textoNuevo,
                onValueChange = onTextoChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Escribe un comentario...", fontSize = 13.sp) },
                maxLines = 3,
                enabled = !enviando
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = onPublicar,
                enabled = textoNuevo.isNotBlank() && !enviando,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (textoNuevo.isNotBlank()) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                if (enviando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Publicar comentario",
                        tint = if (textoNuevo.isNotBlank()) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
