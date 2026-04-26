package uniquindio.marketplace2.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.marketplace2.R
import uniquindio.marketplace2.data.model.OfertasMock
import uniquindio.marketplace2.data.modelos.SolicitudMock
import uniquindio.marketplace2.data.modelos.UsuariosMock

@Composable
fun PantallaDashboard(
    onCrearOfertaClick: () -> Unit,
    onSolicitudesClick: () -> Unit,
    onNotificacionesClick: () -> Unit,
    onOfertaClick: (String) -> Unit
) {
    val usuario = UsuariosMock.trabajadorEjemplo
    val solicitudesPendientes = SolicitudMock.lista.count { it.estado == "pendiente" }
    val solicitudesAceptadas = SolicitudMock.lista.count { it.estado == "aceptada" }
    val ingresosMes = 1240.0
    val serviciosMes = 8

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary).padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(stringResource(R.string.dashboard_bienvenido) + " 👋",
                                color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
                            Text(usuario.nombre, color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier.size(50.dp).clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(30.dp))
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { i ->
                            Icon(Icons.Default.Star, null,
                                tint = if (i < usuario.calificacionPromedio.toInt()) Color(0xFFFCD34D)
                                       else Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(16.dp))
                        }
                        Spacer(Modifier.width(6.dp))
                        Text("%.1f • %d servicios completados".format(
                            usuario.calificacionPromedio, usuario.serviciosCompletados),
                            color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp)
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(stringResource(R.string.dashboard_resumen_mes),
                    fontWeight = FontWeight.Bold, fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricaCard(Modifier.weight(1f), stringResource(R.string.dashboard_ingresos),
                        "$${"%.0f".format(ingresosMes)}", Icons.Default.AttachMoney, Color(0xFF16A34A))
                    MetricaCard(Modifier.weight(1f), stringResource(R.string.dashboard_servicios_mes),
                        serviciosMes.toString(), Icons.Default.CheckCircle, MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricaCard(Modifier.weight(1f), stringResource(R.string.dashboard_pendientes),
                        solicitudesPendientes.toString(), Icons.Default.WatchLater, Color(0xFFD97706))
                    MetricaCard(Modifier.weight(1f), stringResource(R.string.dashboard_en_curso),
                        solicitudesAceptadas.toString(), Icons.Default.Schedule, MaterialTheme.colorScheme.tertiary)
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text(stringResource(R.string.dashboard_acciones_rapidas),
                    fontWeight = FontWeight.Bold, fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AccionCard(Modifier.weight(1f), stringResource(R.string.dashboard_nueva_oferta),
                        Icons.Default.AddCircle, MaterialTheme.colorScheme.primary, onClick = onCrearOfertaClick)
                    AccionCard(Modifier.weight(1f), stringResource(R.string.dashboard_solicitudes),
                        Icons.Default.Inbox, Color(0xFFD97706), badgeCount = solicitudesPendientes, onClick = onSolicitudesClick)
                    AccionCard(Modifier.weight(1f), stringResource(R.string.dashboard_notificaciones),
                        Icons.Default.Notifications, MaterialTheme.colorScheme.tertiary, onClick = onNotificacionesClick)
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.dashboard_solicitudes_recientes), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(onClick = onSolicitudesClick) {
                    Text(stringResource(R.string.dashboard_ver_todas), fontSize = 12.sp)
                }
            }
        }

        val solicitudesRecientes = SolicitudMock.lista.take(3)
        items(solicitudesRecientes) { solicitud ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onSolicitudesClick() },
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(solicitud.ofertaTitulo, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("De: ${solicitud.clienteNombre}", fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Para: ${solicitud.fechaPreferida}", fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    val (bgColor, txtColor, label) = when (solicitud.estado) {
                        "pendiente"  -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "Pendiente")
                        "aceptada"   -> Triple(Color(0xFFDCFCE7), Color(0xFF16A34A), "Aceptada")
                        else         -> Triple(Color(0xFFF3F4F6), Color(0xFF6B7280), "Completada")
                    }
                    Surface(color = bgColor, shape = RoundedCornerShape(8.dp)) {
                        Text(label, color = txtColor, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }
        }

        item {
            Text(stringResource(R.string.dashboard_servicios_activos),
                fontWeight = FontWeight.Bold, fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp))
        }

        val misOfertas = OfertasMock.misOfertas
        if (misOfertas.isEmpty()) {
            item {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddBox, null, Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                        Spacer(Modifier.height(8.dp))
                        Text(stringResource(R.string.dashboard_sin_servicios),
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = onCrearOfertaClick) {
                            Icon(Icons.Default.Add, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.dashboard_publicar_primero))
                        }
                    }
                }
            }
        } else {
            items(misOfertas) { oferta ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { onOfertaClick(oferta.id) },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(when (oferta.categoria) {
                                "Hogar" -> "🏠"; "Educación" -> "📚"
                                "Mascotas" -> "🐾"; "Tecnología" -> "💻"
                                "Transporte" -> "🚗"; else -> "⚙️"
                            }, fontSize = 22.sp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(oferta.titulo, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("$%.0f - $%.0f".format(oferta.precioMin, oferta.precioMax),
                                fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFCD34D), modifier = Modifier.size(14.dp))
                                Text("%.1f".format(oferta.calificacion), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("${oferta.totalResenas} ${stringResource(R.string.dashboard_resenas)}",
                                fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun MetricaCard(modifier: Modifier, titulo: String, valor: String, icono: ImageVector, color: Color) {
    Card(modifier = modifier, elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, null, tint = color, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(titulo, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            Text(valor, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = color)
        }
    }
}

@Composable
private fun AccionCard(
    modifier: Modifier, titulo: String, icono: ImageVector, color: Color,
    badgeCount: Int = 0, onClick: () -> Unit
) {
    Card(modifier = modifier.clickable { onClick() }, elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f))) {
        Column(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                Icon(icono, null, tint = color, modifier = Modifier.size(32.dp))
                if (badgeCount > 0) {
                    Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(Color(0xFFEF4444))
                        .align(Alignment.TopEnd), contentAlignment = Alignment.Center) {
                        Text(badgeCount.toString(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(titulo, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = color)
        }
    }
}
