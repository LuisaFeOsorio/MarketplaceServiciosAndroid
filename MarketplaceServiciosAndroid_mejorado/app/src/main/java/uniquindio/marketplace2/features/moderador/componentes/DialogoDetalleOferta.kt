package uniquindio.marketplace2.features.moderador.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.marketplace2.R
import uniquindio.marketplace2.data.modelos.OfertaPendiente

@Composable
fun DialogoDetalleOferta(
    oferta: OfertaPendiente,
    onDismiss: () -> Unit,
    onAprobar: () -> Unit,
    onRechazar: () -> Unit,
    getColorRecomendacion: (String) -> Color,
    getTextoRecomendacion: (String) -> String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.moderador_revisar_oferta)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Título y proveedor
                Text(oferta.titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("👤 ${oferta.proveedorNombre}", fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary)
                Text(
                    "💰 $${"%,.0f".format(oferta.precioMin)} - $${"%,.0f".format(oferta.precioMax)}",
                    fontSize = 14.sp
                )

                // Descripción original
                HorizontalDivider()
                Text(
                    stringResource(R.string.moderador_descripcion_original),
                    fontWeight = FontWeight.Bold, fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        oferta.descripcion,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp
                    )
                }

                // Análisis IA (opcional)
                oferta.analisisIA?.let { analisis ->
                    HorizontalDivider()
                    Text(
                        stringResource(R.string.moderador_analisis_ia),
                        fontWeight = FontWeight.Bold, fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val color = getColorRecomendacion(analisis.recomendacion)
                    Surface(
                        color = color.copy(alpha = 0.08f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                getTextoRecomendacion(analisis.recomendacion),
                                color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp
                            )
                            if (analisis.palabrasDetectadas.isNotEmpty()) {
                                Text(
                                    "${stringResource(R.string.moderador_palabras_detectadas)} " +
                                            analisis.palabrasDetectadas.joinToString(", "),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Text(
                                "${stringResource(R.string.moderador_confianza)} " +
                                        "${(analisis.puntajeConfianza * 100).toInt()}%",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (analisis.versionSugerida.isNotEmpty()) {
                                Text(
                                    stringResource(R.string.moderador_version_sugerida),
                                    fontSize = 12.sp, fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        analisis.versionSugerida,
                                        modifier = Modifier.padding(8.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRechazar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.moderador_rechazar))
                }
                Button(
                    onClick = onAprobar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.moderador_aprobar))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.moderador_cancelar))
            }
        }
    )
}
