package dev.gemmabcr

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import dev.gemmabcr.models.pokemons.todo.AttackToDoType
import dev.gemmabcr.models.pokemons.todo.CaughtToDoType
import dev.gemmabcr.models.pokemons.todo.DefeatedToDoType
import dev.gemmabcr.models.pokemons.todo.SearchToDoType
import dev.gemmabcr.models.pokemons.todo.SpecificToDo
import dev.gemmabcr.models.pokemons.todo.ToDoType

object Serialization {
    val myArceusSerializersModule = SerializersModule {
        polymorphic(ToDoType::class) {
            subclass(AttackToDoType::class, AttackToDoType.serializer())
            subclass(CaughtToDoType::class, CaughtToDoType.serializer())
            subclass(DefeatedToDoType::class, DefeatedToDoType.serializer())
            subclass(SearchToDoType::class, SearchToDoType.serializer())
            subclass(SpecificToDo::class, SpecificToDo.serializer())
        }
    }
    @OptIn(ExperimentalSerializationApi::class)
    val jsonConfig = Json {
        serializersModule = myArceusSerializersModule
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        prettyPrint = false
        explicitNulls = false
    }
}
