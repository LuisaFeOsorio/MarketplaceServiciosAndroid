package uniquindio.marketplace2.features.ofertas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uniquindio.marketplace2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleOferta(
    ofertaId: String,
    onBackPressed: () -> Unit,
    onSolicitarClick: (String) -> Unit,
    viewModel: DetalleOfertaViewModel = hiltViewModel()
) {
    val oferta by viewModel.oferta.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(ofertaId) {
        viewModel.cargarOferta(ofertaId)
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("😕", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error ?: "Oferta no disponible",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = onBackPressed) {
                            Text(stringResource(R.string.volver))
                        }
                    }
                }
            }
            return
        }

        oferta == null -> {
            return
        }
    }

    val ofertaActual = oferta!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ofertaActual.titulo) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            item {
                Column {
                    Text(ofertaActual.titulo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Row {
                        Text(
                            text = when (ofertaActual.categoria) {
                                "Hogar" -> stringResource(R.string.feed_categoria_hogar)
                                "Educación" -> stringResource(R.string.feed_categoria_educacion)
                                "Mascotas" -> stringResource(R.string.feed_categoria_mascotas)
                                "Tecnología" -> stringResource(R.string.feed_categoria_tecnologia)
                                "Transporte" -> stringResource(R.string.feed_categoria_transporte)
                                else -> ofertaActual.categoria
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

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
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

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = ofertaActual.proveedorNombre,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = stringResource(R.string.feed_calificacion_formato, ofertaActual.calificacion),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column {
                    Text(
                        text = stringResource(R.string.detalle_descripcion),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(ofertaActual.descripcion)
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.detalle_distancia, ofertaActual.distanciaKm))
                }
            }

            if (ofertaActual.etiquetas.isNotEmpty()) {
                item {
                    Column {
                        Text("Etiquetas", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ofertaActual.etiquetas.forEach { tag ->
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "#$tag",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                val resenas = uniquindio.marketplace2.data.model.ResenasMock.obtenerPorOferta(ofertaActual.id)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Reseñas (${resenas.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = androidx.compose.ui.graphics.Color(0xFFFCD34D), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("%.1f".format(ofertaActual.calificacion), fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (resenas.isEmpty()) {
                        Text("Aún no hay reseñas para este servicio.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        resenas.forEach { resena ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = CardDefaults.cardElevation(1.dp)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(resena.autorNombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            repeat(resena.calificacion.toInt()) {
                                                Icon(Icons.Default.Star, null, tint = androidx.compose.ui.graphics.Color(0xFFFCD34D), modifier = Modifier.size(12.dp))
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(resena.comentario, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(resena.fecha, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
