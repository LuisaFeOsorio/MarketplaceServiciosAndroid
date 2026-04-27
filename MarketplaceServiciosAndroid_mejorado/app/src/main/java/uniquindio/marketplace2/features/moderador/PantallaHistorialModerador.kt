package uniquindio.marketplace2.features.moderador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Modelo de evento de historial ───────────────────────────
data class EventoModerador(
    val id: String,
    val tipo: String,
    val tituloOferta: String,
    val proveedor: String,
    val fecha: String,
    val hora: String,
    val motivo: String? = null
)

// ─── Datos mock de historial ──────────────────────────────────
object HistorialMock {
    val eventos = listOf(
        EventoModerador("h1",  "aprobada",  "Reparación de Fugas 24/7",       "Juan Pérez",       "26/04/2026", "09:14"),
        EventoModerador("h2",  "rechazada", "Masajes Relajantes",             "Pedro Romero",     "26/04/2026", "08:52",
            motivo = "Descripción incompleta y precio no especificado"),
        EventoModerador("h3",  "aprobada",  "Clases de Inglés Conversacional","Laura Ramírez",    "25/04/2026", "17:30"),
        EventoModerador("h4",  "eliminada", "Venta de Electrodomésticos",     "Carlos Suárez",    "25/04/2026", "15:10",
            motivo = "Categoría no permitida en la plataforma"),
        EventoModerador("h5",  "aprobada",  "Soporte Técnico a Domicilio",    "Ana Martínez",     "25/04/2026", "11:05"),
        EventoModerador("h6",  "rechazada", "Clases de Guitarra",             "Miguel Torres",    "24/04/2026", "16:44",
            motivo = "Imágenes de baja calidad, solicitar fotos reales"),
        EventoModerador("h7",  "aprobada",  "Mudanzas Locales",               "Transportes García","24/04/2026","10:20"),
        EventoModerador("h8",  "aprobada",  "Veterinaria a Domicilio",        "Dra. Patricia Mora","23/04/2026","14:00"),
        EventoModerador("h9",  "rechazada", "Diseño de Logos Rápidos",        "Fernanda López",   "23/04/2026", "09:35",
            motivo = "El portafolio adjunto no corresponde al servicio descrito"),
        EventoModerador("h10", "aprobada",  "Pintura de Interiores",          "Fernando Castro",  "22/04/2026", "12:15"),
    )
}

// ─── Pantalla principal ───────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHistorialModerador() {
    var filtro by remember { mutableStateOf("todos") }
    val filtros = listOf("todos" to "Todos", "aprobada" to "Aprobadas",
        "rechazada" to "Rechazadas", "eliminada" to "Eliminadas")

    val eventosFiltrados = remember(filtro) {
        if (filtro == "todos") HistorialMock.eventos
        else HistorialMock.eventos.filter { it.tipo == filtro }
    }

    // Contadores resumen
    val totalAprobadas  = HistorialMock.eventos.count { it.tipo == "aprobada" }
    val totalRechazadas = HistorialMock.eventos.count { it.tipo == "rechazada" }
    val totalEliminadas = HistorialMock.eventos.count { it.tipo == "eliminada" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Moderación") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tarjetas de resumen
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TarjetaResumen("Aprobadas", totalAprobadas.toString(),
                        Color(0xFF16A34A), Icons.Default.CheckCircle, Modifier.weight(1f))
                    TarjetaResumen("Rechazadas", totalRechazadas.toString(),
                        Color(0xFFDC2626), Icons.Default.Cancel, Modifier.weight(1f))
                    TarjetaResumen("Eliminadas", totalEliminadas.toString(),
                        Color(0xFF6B7280), Icons.Default.DeleteForever, Modifier.weight(1f))
                }
            }

            // Filtros
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filtros.forEach { (valor, label) ->
                        FilterChip(
                            selected = filtro == valor,
                            onClick = { filtro = valor },
                            label = { Text(label, fontSize = 11.sp) },
                            leadingIcon = if (filtro == valor) {
                                { Icon(Icons.Default.Check, null, Modifier.size(14.dp)) }
                            } else null
                        )
                    }
                }
            }

            item {
                Text(
                    "${eventosFiltrados.size} registro${if (eventosFiltrados.size != 1) "s" else ""}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Lista de eventos
            items(eventosFiltrados, key = { it.id }) { evento ->
                TarjetaEvento(evento)
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

// ─── Tarjeta de resumen ───────────────────────────────────────
@Composable
private fun TarjetaResumen(
    label: String,
    valor: String,
    color: Color,
    icono: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icono, null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(Modifier.height(4.dp))
            Text(valor, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = color)
            Text(label, fontSize = 10.sp, color = color.copy(alpha = 0.8f))
        }
    }
}

// ─── Tarjeta de evento de historial ──────────────────────────
@Composable
private fun TarjetaEvento(evento: EventoModerador) {
    val (color, icono, etiqueta) = when (evento.tipo) {
        "aprobada"  -> Triple(Color(0xFF16A34A), Icons.Default.CheckCircle, "Aprobada")
        "rechazada" -> Triple(Color(0xFFDC2626), Icons.Default.Cancel,      "Rechazada")
        else        -> Triple(Color(0xFF6B7280), Icons.Default.DeleteForever,"Eliminada")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ícono circular de estado
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, null, tint = color, modifier = Modifier.size(22.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(evento.tituloOferta,
                        fontWeight = FontWeight.SemiBold, fontSize = 14.sp, maxLines = 2,
                        modifier = Modifier.weight(1f))
                    Surface(
                        color = color.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(etiqueta, color = color, fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp))
                    }
                }

                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(3.dp))
                    Text(evento.proveedor, fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(3.dp))
                    Text("${evento.fecha}  ${evento.hora}", fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                evento.motivo?.let { motivo ->
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(Icons.Default.Info, null,
                                modifier = Modifier.size(14.dp).padding(top = 1.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.width(4.dp))
                            Text(motivo, fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
