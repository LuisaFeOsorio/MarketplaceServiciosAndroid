package uniquindio.marketplace2.features.moderador

import androidx.compose.ui.graphics.Color

/**
 * Funciones de presentación para el panel de moderación.
 * Separadas del ViewModel para evitar dependencias de Compose en la capa de lógica.
 */
object ModeradorUiHelper {

    fun getColorRecomendacion(recomendacion: String): Color = when (recomendacion) {
        "aprobar"  -> Color(0xFF16A34A)
        "revisar"  -> Color(0xFFD97706)
        "rechazar" -> Color(0xFFDC2626)
        else       -> Color(0xFF6B7280)
    }

    fun getTextoRecomendacion(recomendacion: String): String = when (recomendacion) {
        "aprobar"  -> "✅ Recomendado: Aprobar"
        "revisar"  -> "⚠️ Recomendado: Revisar"
        "rechazar" -> "❌ Recomendado: Rechazar"
        else       -> "Sin análisis"
    }
}
