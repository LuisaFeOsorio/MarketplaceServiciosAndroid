package uniquindio.marketplace2.features.perfil

// ════════════════════════════════════════════════════════════
//  PARCHE PARA PantallaPerfil — sección de puntos de reputación
//  Este bloque se debe insertar dentro de la Column del Scaffold
//  de PantallaPerfil, DESPUÉS del bloque de estadísticas
//  (serviciosCompletados / calificación) y ANTES del botón
//  "Cerrar sesión".
//
//  Si prefieres, simplemente copia el Composable SeccionPuntos
//  a tu PantallaPerfil.kt existente y llámalo desde la pantalla.
// ════════════════════════════════════════════════════════════

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uniquindio.marketplace2.data.repositorios.RepositorioUsuarios

// ════════════════════════════════════════════════════════════
//  Composable reutilizable: muestra puntos + nivel + barra
// ════════════════════════════════════════════════════════════
@Composable
fun SeccionPuntos(puntos: Int) {
    val repositorioUsuarios = RepositorioUsuarios()   // En prod: inyectar vía ViewModel
    val nivel  = repositorioUsuarios.obtenerNivel(puntos)
    val progreso = when {
        puntos >= 500 -> 1f
        puntos >= 200 -> (puntos - 200) / 300f
        puntos >= 50  -> (puntos - 50) / 150f
        else          -> puntos / 50f
    }.coerceIn(0f, 1f)

    val puntosParaSiguiente = when {
        puntos >= 500 -> null        // ya es Experto
        puntos >= 200 -> 500
        puntos >= 50  -> 200
        else          -> 50
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Encabezado
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFCD34D),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Reputación", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        nivel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Puntos actuales
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "$puntos",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "puntos",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Barra de progreso
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f)
            )

            Spacer(Modifier.height(6.dp))

            // Leyenda
            if (puntosParaSiguiente != null) {
                Text(
                    "Faltan ${puntosParaSiguiente - puntos} puntos para el siguiente nivel",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            } else {
                Text(
                    "¡Nivel máximo alcanzado! 🎉",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f))
            Spacer(Modifier.height(10.dp))

            // Cómo ganar puntos
            Text("Cómo ganar puntos:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            val reglas = listOf(
                "⭐ Crear una oferta" to "+5 pts",
                "✅ Completar un servicio" to "+20 pts",
                "💬 Recibir una reseña" to "+10 pts",
                "❤️ Recibir un voto 'Me interesa'" to "+2 pts",
                "📝 Publicar un comentario" to "+1 pt"
            )
            reglas.forEach { (accion, puntosTxt) ->
                Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(accion, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text(puntosTxt, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
