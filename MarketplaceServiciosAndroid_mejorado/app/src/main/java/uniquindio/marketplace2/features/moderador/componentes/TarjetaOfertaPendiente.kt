package uniquindio.marketplace2.features.moderador.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.marketplace2.R
import uniquindio.marketplace2.data.modelos.OfertaPendiente

@Composable
fun TarjetaOfertaPendiente(
    oferta: OfertaPendiente,
    onClick: () -> Unit,
    getColorRecomendacion: (String) -> Color,
    getTextoRecomendacion: (String) -> String
) {
    val emoji = when (oferta.categoria) {
        "Hogar"      -> "🏠"
        "Educación"  -> "📚"
        "Mascotas"   -> "🐾"
        "Tecnología" -> "💻"
        "Transporte" -> "🚗"
        else         -> "⚙️"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Encabezado: título + badge de categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$emoji ${oferta.titulo}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = oferta.categoria,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Proveedor y precio
            Text(
                text = "👤 ${oferta.proveedorNombre}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "💰 $${"%,.0f".format(oferta.precioMin)} - $${"%,.0f".format(oferta.precioMax)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Resumen del análisis IA (solo si existe)
            oferta.analisisIA?.let { analisis ->
                val color = getColorRecomendacion(analisis.recomendacion)
                Surface(
                    color = color.copy(alpha = 0.08f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🤖", fontSize = 12.sp)
                            Text(
                                " ${getTextoRecomendacion(analisis.recomendacion)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = color
                            )
                        }
                        if (analisis.palabrasDetectadas.isNotEmpty()) {
                            Text(
                                "⚠️ ${stringResource(R.string.moderador_palabras_detectadas)} " +
                                        analisis.palabrasDetectadas.joinToString(", "),
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
