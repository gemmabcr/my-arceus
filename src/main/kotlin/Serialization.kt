package dev.gemmabcr

import dev.gemmabcr.models.pokemons.todo.AttackToDo
import dev.gemmabcr.models.pokemons.todo.CaughtToDo
import dev.gemmabcr.models.pokemons.todo.DefeatedToDo
import dev.gemmabcr.models.pokemons.todo.SearchToDo
import dev.gemmabcr.models.pokemons.todo.SpecificToDo
import dev.gemmabcr.models.pokemons.todo.ToDo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

object Serialization {
    val myArceusSerializersModule = SerializersModule {
        polymorphic(ToDo::class) {
            subclass(AttackToDo::class, AttackToDo.serializer())
            subclass(CaughtToDo::class, CaughtToDo.serializer())
            subclass(DefeatedToDo::class, DefeatedToDo.serializer())
            subclass(SearchToDo::class, SearchToDo.serializer())
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