package dev.gemmabcr.views.pages.components

import kotlinx.html.FlowContent
import kotlinx.html.img
import kotlinx.html.style

class PokemonImage(private val id: Int) : View {
    private val url = "https://raw.githubusercontent.com/HybridShivam/Pokemon/master/assets/images/"
    private val hisuiImages = setOf(
        GROWLITHE,
        ARCANINE,
        VOLTORB,
        ELECTRODE,
        TYPHLOSION,
        LILLIGANT,
        ZORUA,
        ZOROARK,
        BRAVIARY,
        SLIGGO,
        GOODRA,
        AVALUGG,
        DECIDUEYE
    )

    override fun create(content: FlowContent) = content.apply {
        img(src = url(id)) {
            style = "height: 120px; width: 120px;"
        }
    }

    private fun url(generalId: Int): String {
        val number = generalId.toString().padStart(PAD_START_LENGTH, '0')
        return "$url$number${maybeIsHisui(generalId)}.png"
    }

    private fun maybeIsHisui(generalId: Int): String = when (hisuiImages.contains(generalId)) {
        true -> "-Hisui"
        false -> ""
    }
}

private const val PAD_START_LENGTH = 3
private const val GROWLITHE = 58
private const val ARCANINE = 59
private const val VOLTORB = 100
private const val ELECTRODE = 101
private const val TYPHLOSION = 157
private const val LILLIGANT = 549
private const val ZORUA = 570
private const val ZOROARK = 571
private const val BRAVIARY = 628
private const val SLIGGO = 705
private const val GOODRA = 706
private const val AVALUGG = 713
private const val DECIDUEYE = 724
