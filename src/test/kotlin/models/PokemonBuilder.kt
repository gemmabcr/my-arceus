package models

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.Location
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.SpecialCondition
import dev.gemmabcr.models.ToDo
import dev.gemmabcr.models.Type

class PokemonBuilder {
    private var hisuiId: Int = 1
    private val generalId: Int = 234
    private val name: String = "Pikachu"
    private val types: List<Type> = listOf(Type.ELECTRIC)
    private var location: Location = LocationBuilder().build()
    private val toDos: List<ToDo> = listOf(ToDo("usar tipo veneno", 25))
    private val specialCondition: SpecialCondition? = null

    fun with(number: Int) = apply {
        hisuiId = number
    }

    fun with(area: Area) = apply {
        location = LocationBuilder().with(area).build()
    }

    fun build(): Pokemon = Pokemon(
        hisuiId,
        generalId,
        name,
        types,
        listOf(location),
        toDos,
        specialCondition,
    )
}
