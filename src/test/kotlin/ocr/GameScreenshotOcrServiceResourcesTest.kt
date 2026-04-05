package ocr

import dev.gemmabcr.ocr.GameScreenshotOcrService
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameScreenshotOcrServiceResourcesTest {
    private val service = GameScreenshotOcrService()

    @Test
    fun allBundledScreenshotsHaveAnExpectedReferenceAndProduceProgress() {
        val screenshotFiles = File("src/test/resources/ocr")
            .listFiles { file -> file.isFile && file.extension.equals("JPG", ignoreCase = true) }
            ?.sortedBy { it.nameWithoutExtension.toInt() }
            .orEmpty()

        assertTrue(screenshotFiles.isNotEmpty(), "No OCR screenshots found in test resources.")
        assertEquals(screenshotFiles.map { it.name }.sorted(), expectedScreenshots.keys.sorted())

        screenshotFiles.forEach { file ->
            val expectation = expectedScreenshots.getValue(file.name)
            val result = service.extractPokedexProgress(file.readBytes(), file.name)
            val actualLabels = result.tasks.map { normalizeLabel(it.label) }
            val expectedLabels = expectation.tasks.map(::normalizeLabel)

            println(
                "OCR ${file.name}: expectedNumber=${expectation.number}, " +
                        "actualNumber=${result.pokemonNumber}, actualName=${result.pokemonName}, " +
                        "progress=${result.progressLevel}, tasks=${result.tasks}"
            )

            assertNotNull(result.progressLevel, "Missing progress level for ${file.name}")
            assertEquals(expectedLabels.size, actualLabels.size, "Unexpected task count for ${file.name}")
            expectedLabels.zip(actualLabels).forEachIndexed { index, (expectedLabel, actualLabel) ->
                assertTrue(
                    labelsAreEquivalent(expectedLabel, actualLabel),
                    "Unexpected task label at position $index for ${file.name}. " +
                            "Expected '$expectedLabel' but was '$actualLabel'"
                )
            }
        }
    }

    private fun normalizeLabel(value: String): String =
        value.lowercase()
            .replace(Regex("""\p{M}+"""), "")
            .replace(Regex("""[^a-z0-9]+"""), " ")
            .replace(Regex("""\s+"""), " ")
            .trim()

    private fun labelsAreEquivalent(expectedLabel: String, actualLabel: String): Boolean =
        expectedLabel == actualLabel ||
                actualLabel.startsWith(expectedLabel) ||
                expectedLabel.startsWith(actualLabel) ||
                hasStrongTokenOverlap(expectedLabel, actualLabel)

    private fun hasStrongTokenOverlap(expectedLabel: String, actualLabel: String): Boolean {
        val expectedTokens = expectedLabel.split(" ").filter { it.isNotBlank() }
        val actualTokens = actualLabel.split(" ").filter { it.isNotBlank() }.toSet()
        val commonTokens = expectedTokens.count { token -> actualTokens.contains(token) }
        val minimumCommonTokens = (expectedTokens.size - TOKEN_TOLERANCE).coerceAtLeast(MIN_COMMON_TOKENS)
        return commonTokens >= minimumCommonTokens
    }
}

private data class ExpectedScreenshotOcr(
    val number: Int,
    val tasks: List<String>
)

private const val TOKEN_TOLERANCE = 2
private const val MIN_COMMON_TOKENS = 2

private val expectedScreenshots = mapOf(
    "36.JPG" to ExpectedScreenshotOcr(
        number = 36,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares alfa capturados",
            "Ejemplares capturados en el aire",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Hielo",
            "Veces que lo has visto usar Veneno X",
            "Veces que lo has visto usar Chupavidas",
            "Veces que ha usado movimientos con estilo rapido"
        )
    ),
    "48.JPG" to ExpectedScreenshotOcr(
        number = 48,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares alfa capturados",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Acero",
            "Veces que lo has visto usar Trampa Rocas",
            "Veces que lo has visto usar Doble Filo",
            "Veces que ha usado movimientos con estilo fuerte"
        )
    ),
    "69.JPG" to ExpectedScreenshotOcr(
        number = 69,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Planta",
            "Veces que lo has visto usar Cabezazo Zen",
            "Veces que lo has visto usar Acua Cola",
            "Veces que ha usado movimientos con estilo rapido",
            "Veces que lo has fatigado",
            "Veces que lo has asustado con una Bola Ruidosa"
        )
    ),
    "92.JPG" to ExpectedScreenshotOcr(
        number = 92,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares alfa capturados",
            "Ejemplares grandes capturados",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Volador",
            "Veces que lo has visto usar Bomba Acida",
            "Veces que ha usado movimientos con estilo fuerte",
            "Veces que lo has fatigado"
        )
    ),
    "99.JPG" to ExpectedScreenshotOcr(
        number = 99,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Psíquico",
            "Veces que lo has visto usar Bomba Fango",
            "Veces que lo has asustado con una Bola Ruidosa",
            "Variaciones de forma y aspecto avistadas",
            "Ejemplares evolucionados",
            "Investiga acerca del veneno de Croagunk"
        )
    ),
    "107.JPG" to ExpectedScreenshotOcr(
        number = 107,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares capturados sin que te detecten",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Planta",
            "Veces que lo has visto usar Mordisco",
            "Veces que lo has alimentado",
            "Variaciones de forma y aspecto avistadas",
            "Ejemplares evolucionados"
        )
    ),
    "167.JPG" to ExpectedScreenshotOcr(
        number = 167,
        tasks = listOf(
            "Ejemplares capturados",
            "Veces que lo has visto usar Envite Acuático",
            "Veces que lo has visto usar Bola Sombra",
            "Veces que ha usado movimientos con estilo fuerte",
            "Veces que ha usado movimientos con estilo rapido"
        )
    ),
    "186.JPG" to ExpectedScreenshotOcr(
        number = 186,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares alfa capturados",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Agua",
            "Veces que lo has visto usar Bomba Fango",
            "Veces que lo has visto usar Tijera X",
            "Veces que ha usado movimientos con estilo fuerte",
            "Veces que lo has fatigado"
        )
    ),
    "187.JPG" to ExpectedScreenshotOcr(
        number = 187,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares pesados capturados",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Hielo",
            "Veces que lo has visto usar Ciclón",
            "Veces que lo has visto usar Cuchillada",
            "Variaciones de forma y aspecto avistadas",
            "Ejemplares evolucionados"
        )
    ),
    "190.JPG" to ExpectedScreenshotOcr(
        number = 190,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares pesados capturados",
            "Ejemplares capturados sin que te detecten",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Planta",
            "Veces que lo has visto usar Defensa Férrea",
            "Ejemplares avistados saliendo de rocas agrietadas",
            "Ejemplares evolucionados",
            "Investiga el proverbio acerca de Nosepass"
        )
    ),
    "196.JPG" to ExpectedScreenshotOcr(
        number = 196,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares ligeros capturados",
            "Ejemplares alfa capturados",
            "Ejemplares derrotados",
            "Veces que lo has visto usar Viento Aciago",
            "Veces que lo has visto usar Paranormal",
            "Veces que ha usado movimientos con estilo rapido",
            "Investiga el caso del Chimecho instalado bajo el alero"
        )
    ),
    "198.JPG" to ExpectedScreenshotOcr(
        number = 198,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares alfa capturados",
            "Ejemplares derrotados",
            "Ejemplares derrotados con movimientos de tipo Siniestro",
            "Veces que lo has visto usar Infortunio",
            "Veces que lo has visto usar Joya de Luz",
            "Veces que lo has fatigado",
            "Veces que ha usado movimientos con estilo rapido"
        )
    ),
    "202.JPG" to ExpectedScreenshotOcr(
        number = 202,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares ligeros capturados",
            "Ejemplares derrotados",
            "Veces que lo has visto usar Cuchillada",
            "Veces que lo has alimentado",
            "Variaciones de forma y aspecto avistadas",
            "Ejemplares evolucionados"
        )
    ),
    "210.JPG" to ExpectedScreenshotOcr(
        number = 210,
        tasks = listOf(
            "Ejemplares capturados",
            "Ejemplares pesados capturados",
            "Veces que lo has visto usar Poder Pasado",
            "Ejemplares evolucionados"
        )
    ),
    "211.JPG" to ExpectedScreenshotOcr(
        number = 211,
        tasks = listOf(
            "Ejemplares capturados",
            "Veces que lo has visto usar Defensa Férrea",
            "Veces que lo has visto usar Tierra Viva",
            "Veces que lo has visto usar Trampa Rocas",
            "Veces que ha usado movimientos con estilo rápido"
        )
    )
)
