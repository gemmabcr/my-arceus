package dev.gemmabcr.models

enum class Type(
    private val text: String,
) {
    BUG("Bicho"),
    DARK("Siniestro"),
    DRAGON("Dragón"),
    ELECTRIC("Eléctrico"),
    FAIRY("Hada"),
    FIGHTING("Lucha"),
    FIRE("Fuego"),
    FLYING("Volador"),
    GHOST("Fantasma"),
    GRASS("Planta"),
    GROUND("Tierra"),
    ICE("Hielo"),
    NORMAL("Normal"),
    POISON("Veneno"),
    PSYCHIC("Psíquico"),
    ROCK("Roca"),
    STEEL("Acero"),
    WATER("Agua");

    override fun toString(): String = text
}
