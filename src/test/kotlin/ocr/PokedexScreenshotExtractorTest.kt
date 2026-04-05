package ocr

import dev.gemmabcr.ocr.PokedexScreenshotExtractor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PokedexScreenshotExtractorTest {
    private val extractor = PokedexScreenshotExtractor()

    @Test
    fun extractsPokemonHeaderProgressAndTasksFromSpanishOcrBlocks() {
        val result = extractor.extract(
            titleText = "Tareas de la Pokédex de Crobat",
            tasksText = """
                Ejemplares capturados 5 v1 v2 v3 v4 v5
                Ejemplares alfa capturados 3 v1 v2 v3
                Ejemplares capturados en el aire 0 1 2 3 4 5
                Veces que lo has visto usar Veneno X 22 v1 v3 v6 v12 25
                Veces que ha usado movimientos con estilo rapido 58 v1 v3 v10 v30 70
            """.trimIndent(),
            sidebarText = """
                N.° 036
                Crobat
                N.° 092
                Carnivine
            """.trimIndent(),
            selectedSidebarText = """
                N.° 036
                Crobat
            """.trimIndent(),
            progressText = "Nivel de progreso Completado! 10"
        )

        assertEquals("Crobat", result.pokemonName)
        assertEquals(36, result.pokemonNumber)
        assertEquals(10, result.progressLevel)
        assertEquals(5, result.tasks.first { it.label == "Ejemplares capturados" }.value)
        assertEquals(22, result.tasks.first { it.label.contains("Veneno X") }.value)
        assertEquals(58, result.tasks.first { it.label.contains("estilo rapido") }.value)
        assertTrue(result.warning == null)
    }

    @Test
    fun fallsBackToSidebarNameAndEmitsWarningWhenDataIsMissing() {
        val result = extractor.extract(
            titleText = "",
            tasksText = "",
            sidebarText = """
                N.° 123
                Bonsly
            """.trimIndent(),
            selectedSidebarText = """
                N.° 123
                Bonsly
            """.trimIndent(),
            progressText = ""
        )

        assertEquals("Bonsly", result.pokemonName)
        assertEquals(123, result.pokemonNumber)
        assertEquals(null, result.progressLevel)
        assertTrue(result.tasks.isEmpty())
        assertNotNull(result.warning)
    }

    @Test
    fun usesPokemonNameToPickTheCorrectSidebarNumber() {
        val result = extractor.extract(
            titleText = "Tareas de la Pokédex de Golduck",
            tasksText = "Ejemplares capturados 15",
            sidebarText = """
                N.° 048
                Golem
                N.° 069
                Golduck
                N.° 107
                Hippopotas
            """.trimIndent(),
            selectedSidebarText = """
                N.° 069
                Golduck
            """.trimIndent(),
            progressText = "Nivel de progreso Completado! 10"
        )

        assertEquals("Golduck", result.pokemonName)
        assertEquals(69, result.pokemonNumber)
    }

    @Test
    fun usesSelectedSidebarNumberToRecoverNameFromFullSidebar() {
        val result = extractor.extract(
            titleText = "",
            tasksText = "Ejemplares capturados 15",
            sidebarText = """
                N.° 048
                Golem
                N.° 069
                Golduck
                N.° 107
                Hippopotas
            """.trimIndent(),
            selectedSidebarText = "N.° 069",
            progressText = "Nivel de progreso Completado! 10"
        )

        assertEquals("Golduck", result.pokemonName)
        assertEquals(69, result.pokemonNumber)
    }
}
