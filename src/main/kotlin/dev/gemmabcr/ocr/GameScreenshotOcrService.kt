package dev.gemmabcr.ocr

import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.io.path.createTempFile

class GameScreenshotOcrService(
    private val command: String = DEFAULT_TESSERACT_COMMAND
) {
    fun extractPokedexProgress(
        imageBytes: ByteArray,
        fileName: String
    ): PokedexScreenshotOcrResult {
        val image = ImageIO.read(imageBytes.inputStream())
            ?: error("Can not read image")

        val sidebarImage = crop(image, SIDEBAR_RECTANGLE)
        val titleText = runOcr(crop(image, TITLE_RECTANGLE), fileName, psm = SEVEN_PSM)
        val tasksText = runOcr(crop(image, TASKS_RECTANGLE), fileName, psm = SIX_PSM)
        val sidebarText = runOcr(sidebarImage, fileName, psm = SIX_PSM)
        val selectedSidebarText = runOcr(detectSelectedSidebarRow(sidebarImage), fileName, psm = SIX_PSM)
        val progressText = runOcr(crop(image, PROGRESS_RECTANGLE), fileName, psm = SIX_PSM)

        return PokedexScreenshotExtractor().extract(
            titleText = titleText,
            tasksText = tasksText,
            sidebarText = sidebarText,
            selectedSidebarText = selectedSidebarText,
            progressText = progressText
        )
    }

    private fun runOcr(
        image: BufferedImage,
        originalFileName: String,
        psm: Int
    ): String {
        val extension = originalFileName.substringAfterLast('.', "png")
        val imagePath = createTempFile(prefix = "my-arceus-ocr-", suffix = ".$extension").toFile()

        return try {
            ImageIO.write(preprocess(image), extension, imagePath)

            val process = ProcessBuilder(
                command,
                imagePath.absolutePath,
                "stdout",
                "--psm",
                psm.toString(),
                "-l",
                OCR_LANGUAGE
            )
                .start()

            val output = process.inputStream.bufferedReader().use { it.readText() }.trim()
            val errorOutput = process.errorStream.bufferedReader().use { it.readText() }.trim()
            val exitCode = process.waitFor()

            when {
                exitCode != 0 -> error(
                    errorOutput.ifBlank { "OCR execution failed." }
                )

                else -> output
            }
        } catch (_: IOException) {
            error("OCR execution failed.")
        } finally {
            imagePath.delete()
        }
    }

    private fun crop(image: BufferedImage, rectangle: RelativeRectangle): BufferedImage {
        val x = (image.width * rectangle.x).toInt().coerceIn(0, image.width - 1)
        val y = (image.height * rectangle.y).toInt().coerceIn(0, image.height - 1)
        val width = (image.width * rectangle.width).toInt().coerceAtLeast(1).coerceAtMost(image.width - x)
        val height = (image.height * rectangle.height).toInt().coerceAtLeast(1).coerceAtMost(image.height - y)
        return image.getSubimage(x, y, width, height)
    }

    private fun preprocess(image: BufferedImage): BufferedImage {
        val scaledWidth = image.width * IMAGE_SCALE_FACTOR
        val scaledHeight = image.height * IMAGE_SCALE_FACTOR
        val scaled = BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_BYTE_GRAY)
        val graphics = scaled.createGraphics()
        graphics.apply {
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            drawImage(image, INITIAL_POINT, INITIAL_POINT, scaledWidth, scaledHeight, null)
            dispose()
        }

        val normalized = BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_BYTE_GRAY)
        for (y in 0 until scaledHeight) {
            for (x in 0 until scaledWidth) {
                val gray = scaled.raster.getSample(x, y, 0)
                val value = ((gray - CONTRAST_OFFSET) * CONTRAST_FACTOR).toInt()
                    .coerceIn(MIN_COLOR_VALUE, MAX_COLOR_VALUE)
                normalized.raster.setSample(x, y, 0, value)
            }
        }
        return normalized
    }

    private fun detectSelectedSidebarRow(sidebarImage: BufferedImage): BufferedImage {
        val arrowStartX = (sidebarImage.width * SIDEBAR_ARROW_SCAN_START_X).toInt()
        val arrowEndX = (sidebarImage.width * SIDEBAR_ARROW_SCAN_END_X).toInt().coerceAtMost(sidebarImage.width)
        val rowHeight = (sidebarImage.height * SIDEBAR_SELECTED_ROW_HEIGHT).toInt().coerceAtLeast(1)
        val halfWindow = rowHeight / 2

        var bestCenterY = sidebarImage.height / 2
        var bestYellowScore = Double.NEGATIVE_INFINITY

        for (centerY in halfWindow until (sidebarImage.height - halfWindow)) {
            var yellowScore = 0L
            var pixels = 0

            for (y in (centerY - halfWindow) until (centerY + halfWindow)) {
                for (x in arrowStartX until arrowEndX) {
                    val rgb = sidebarImage.getRGB(x, y)
                    val red = rgb shr RED_SHIFT and COLOR_MASK
                    val green = rgb shr GREEN_SHIFT and COLOR_MASK
                    val blue = rgb and COLOR_MASK
                    yellowScore += red + green - blue
                    pixels++
                }
            }

            val averageYellowScore = yellowScore.toDouble() / pixels.coerceAtLeast(1)
            if (averageYellowScore > bestYellowScore) {
                bestYellowScore = averageYellowScore
                bestCenterY = centerY
            }
        }

        val top = (bestCenterY - halfWindow).coerceAtLeast(0)
        val height = rowHeight.coerceAtMost(sidebarImage.height - top)
        return sidebarImage.getSubimage(0, top, sidebarImage.width, height)
    }
}

private const val INITIAL_POINT = 0
private const val IMAGE_SCALE_FACTOR = 2
private const val CONTRAST_OFFSET = 110
private const val CONTRAST_FACTOR = 1.35
private const val MIN_COLOR_VALUE = 0
private const val MAX_COLOR_VALUE = 255
private const val RED_SHIFT = 16
private const val GREEN_SHIFT = 8
private const val COLOR_MASK = 0xFF
private const val DEFAULT_TESSERACT_COMMAND = "tesseract"
private const val OCR_LANGUAGE = "spa+eng"
private const val SIDEBAR_ARROW_SCAN_START_X = 0.0
private const val SIDEBAR_ARROW_SCAN_END_X = 0.12
private const val SIDEBAR_SELECTED_ROW_HEIGHT = 0.22

private val TITLE_RECTANGLE = RelativeRectangle(
    x = 0.23,
    y = 0.06,
    width = 0.46,
    height = 0.08
)
private val TASKS_RECTANGLE = RelativeRectangle(
    x = 0.03,
    y = 0.16,
    width = 0.66,
    height = 0.50
)
private val SIDEBAR_RECTANGLE = RelativeRectangle(
    x = 0.76,
    y = 0.20,
    width = 0.21,
    height = 0.48
)
private val PROGRESS_RECTANGLE = RelativeRectangle(
    x = 0.48,
    y = 0.74,
    width = 0.26,
    height = 0.14
)

private const val SIX_PSM = 6
private const val SEVEN_PSM = 7
