package uniquindio.marketplace2.features.misOfertas

import uniquindio.marketplace2.R
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.marketplace2.data.modelos.Oferta
import uniquindio.marketplace2.data.modelos.OfertasMock

// ════════════════════════════════════════════════════════════
//  CAMBIOS RESPECTO AL ORIGINAL:
//  • Botón "Editar" en cada TarjetaMiOferta
//  • Botón "Eliminar" en cada TarjetaMiOferta con diálogo de confirmación
//  • Estado local mutable para reflejar la eliminación en la UI
//  • onEditarOfertaClick(ofertaId) → nuevo callback
// ════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMisOfertas(
    onCrearOfertaClick: () -> Unit,
    onOfertaClick: (String) -> Unit,
    onEditarOfertaClick: (String) -> Unit,      // NUEVO
    onEliminarOferta: (String) -> Unit = {}     // NUEVO (opcional, para conectar con VM)
) {
    var filtroActivo by remember { mutableStateOf("activa") }
    val filtros = listOf("activa" to "Activas", "inactiva" to "Inactivas", "pendiente" to "En revisión")

    // Estado mutable local para reflejar eliminaciones sin ViewModel
    val todasMisOfertas = remember {
        mutableStateListOf(
            OfertasMock.lista[0].copy(estado = "activa"),
            OfertasMock.lista[0].copy(id = "1b", titulo = "Detección de Filtraciones", estado = "inactiva"),
            OfertasMock.lista[3].copy(id = "4b", titulo = "Revisión Eléctrica Express", estado = "pendiente",
                proveedorId = "2", proveedorNombre = "Juan Pérez")
        )
    }

    // Oferta seleccionada para confirmar eliminación
    var ofertaAEliminar by remember { mutableStateOf<Oferta?>(null) }

    val ofertasFiltradas = todasMisOfertas.filter { it.estado == filtroActivo }

    // ── Diálogo de confirmación de eliminación ────────────────
    ofertaAEliminar?.let { oferta ->
        AlertDialog(
            onDismissRequest = { ofertaAEliminar = null },
            icon = { Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Eliminar oferta?") },
            text = {
                Text("Vas a eliminar \"${oferta.titulo}\". Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        todasMisOfertas.removeIf { it.id == oferta.id }
                        onEliminarOferta(oferta.id)
                        ofertaAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { ofertaAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCrearOfertaClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva oferta", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Estadísticas rápidas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EstadisticaItem("${todasMisOfertas.count { it.estado == "activa" }}", "Activas", Color.White)
                EstadisticaItem("${todasMisOfertas.count { it.estado == "inactiva" }}", "Inactivas", Color.White.copy(alpha = 0.7f))
                EstadisticaItem("${todasMisOfertas.count { it.estado == "pendiente" }}", "En revisión", Color(0xFFFCD34D))
            }

            // Filtros tab
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filtros.forEach { (estado, label) ->
                    val selected = filtroActivo == estado
                    FilterChip(
                        selected = selected,
                        onClick = { filtroActivo = estado },
                        label = { Text(label, fontSize = 12.sp) },
                        leadingIcon = if (selected) {
                            { Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp)) }
                        } else null
                    )
                }
            }

            if (ofertasFiltradas.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.List, null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                        Spacer(Modifier.height(12.dp))
                        Text("No tienes ofertas ${filtros.find { it.first == filtroActivo }?.second?.lowercase()}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (filtroActivo == "activa") {
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = onCrearOfertaClick) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(stringResource(R.string.mis_ofertas_crear))
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(ofertasFiltradas) { oferta ->
                        TarjetaMiOferta(
                            oferta   = oferta,
                            onClick  = { onOfertaClick(oferta.id) },
                            onEditar = { onEditarOfertaClick(oferta.id) },       // NUEVO
                            onEliminar = { ofertaAEliminar = oferta }            // NUEVO
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════
//  Tarjeta de oferta propia — con botones Editar y Eliminar
// ════════════════════════════════════════════════════════════
@Composable
private fun TarjetaMiOferta(
    oferta: Oferta,
    onClick: () -> Unit,
    onEditar: () -> Unit,       // NUEVO
    onEliminar: () -> Unit      // NUEVO
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {

            // Cabecera: ícono + título + badge estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            when (oferta.categoria) {
                                "Hogar" -> "🏠"; "Educación" -> "📚"
                                "Mascotas" -> "🐾"; "Tecnología" -> "💻"
                                "Transporte" -> "🚗"; else -> "⚙️"
                            },
                            fontSize = 20.sp
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(oferta.titulo, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 2)
                        Text(oferta.categoria, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }

                val (badgeColor, badgeText) = when (oferta.estado) {
                    "activa"    -> Color(0xFF16A34A) to "Activa"
                    "inactiva"  -> Color(0xFF6B7280) to "Inactiva"
                    "pendiente" -> Color(0xFFD97706) to "En revisión"
                    "rechazada" -> Color(0xFFDC2626) to "Rechazada"
                    else        -> Color(0xFF6B7280) to oferta.estado
                }
                Surface(color = badgeColor.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                    Text(
                        badgeText, color = badgeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(Modifier.height(10.dp))

            // Precio + calificación
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AttachMoney, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "$%.0f - $%.0f".format(oferta.precioMin, oferta.precioMax),
                        fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, modifier = Modifier.size(14.dp), tint = Color(0xFFFCD34D))
                    Text("%.1f".format(oferta.calificacion), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(" (${oferta.totalResenas})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Etiquetas
            if (oferta.etiquetas.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    oferta.etiquetas.take(3).forEach { tag ->
                        Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp)) {
                            Text(
                                "#$tag", fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // ── NUEVO: Botones Editar / Eliminar ─────────────
            Spacer(Modifier.height(10.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Editar
                OutlinedButton(
                    onClick = onEditar,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Editar", fontSize = 13.sp)
                }
                // Botón Eliminar
                OutlinedButton(
                    onClick = onEliminar,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = ButtonDefaults.outlinedButtonBorder.copy()
                ) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar", fontSize = 13.sp, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun EstadisticaItem(valor: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = color)
        Text(label, fontSize = 11.sp, color = color.copy(alpha = 0.85f))
    }
}
