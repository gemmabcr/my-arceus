package dev.gemmabcr.views.ui

enum class ImageSource(val url: String) {
    FOOTER(
        "https://www.nintendo.com/eu/media/images/10_share_images/" +
                "games_15/nintendo_switch_4/H2x1_NSwitch_PokemonLegendsArceus_ES_image1600w.jpg"
    );

    override fun toString(): String = url
}
