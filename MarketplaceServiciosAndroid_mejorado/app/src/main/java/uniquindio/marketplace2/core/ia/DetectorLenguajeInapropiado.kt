package uniquindio.marketplace2.core.ia

import kotlinx.coroutines.delay
import uniquindio.marketplace2.data.modelos.AnalisisIA

/**
 * Detector de lenguaje inapropiado - VERSIÓN SIMULADA (sin API externa)
 * Funciona con reglas locales, no necesita conexión a internet
 */
class DetectorLenguajeInapropiado {

    // Lista de palabras inapropiadas para detectar
    private val palabrasInapropiadas = listOf(
        "chapucero", "chapuceros", "inútil", "inútiles",
        "no saben", "no sirven", "malísimo", "torpe", "torpes",
        "no me importa", "da igual", "estafa", "tramposo", "mentiroso",
        "odio", "detesto", "aborrezco", "basura", "pésimo"
    )

    private val frasesSospechosas = listOf(
        "los demás", "otros no", "competencia", "la competencia",
        "el mejor", "el único", "nadie más"
    )

    /**
     * Analiza el texto y detecta lenguaje inapropiado
     * VERSIÓN SIMULADA - No usa API externa
     */
    suspend fun analizarTexto(titulo: String, descripcion: String): AnalisisIA {
        // Simular tiempo de procesamiento
        delay(300)

        val textoCompleto = "$titulo $descripcion".lowercase()
        val palabrasDetectadas = mutableListOf<String>()

        // Detectar palabras inapropiadas
        for (palabra in palabrasInapropiadas) {
            if (textoCompleto.contains(palabra)) {
                palabrasDetectadas.add(palabra)
            }
        }

        // Detectar frases sospechosas
        for (frase in frasesSospechosas) {
            if (textoCompleto.contains(frase)) {
                palabrasDetectadas.add(frase)
            }
        }

        val tieneLenguajeInapropiado = palabrasDetectadas.isNotEmpty()

        // Generar versión sugerida
        val versionSugerida = if (tieneLenguajeInapropiado) {
            generarVersionSugerida(descripcion)
        } else {
            ""
        }

        val puntajeConfianza = if (tieneLenguajeInapropiado) {
            0.85f + (palabrasDetectadas.size * 0.03f).coerceAtMost(0.15f)
        } else {
            0.95f
        }

        val recomendacion = when {
            !tieneLenguajeInapropiado -> "aprobar"
            palabrasDetectadas.size >= 3 -> "rechazar"
            else -> "revisar"
        }

        return AnalisisIA(
            tieneLenguajeInapropiado = tieneLenguajeInapropiado,
            palabrasDetectadas = palabrasDetectadas.distinct(),
            versionSugerida = versionSugerida,
            puntajeConfianza = puntajeConfianza,
            recomendacion = recomendacion
        )
    }

    private fun generarVersionSugerida(descripcion: String): String {
        var textoLimpio = descripcion
            .replace(Regex("(?i)los demás son unos chapuceros"), "trabajo profesional")
            .replace(Regex("(?i)no saben nada"), "tengo experiencia")
            .replace(Regex("(?i)no me importa"), "estoy preparado para")
            .replace(Regex("(?i)el mejor"), "experimentado")
            .replace(Regex("(?i)nadie más"), "con amplia experiencia")

        return textoLimpio.trim()
    }

    fun tieneLenguajeInapropiado(texto: String): Boolean {
        val textoLower = texto.lowercase()
        return palabrasInapropiadas.any { textoLower.contains(it) } ||
                frasesSospechosas.any { textoLower.contains(it) }
    }
}