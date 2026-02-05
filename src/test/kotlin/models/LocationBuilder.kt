package models

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.Location

class LocationBuilder {
    private val id: Int = 34
    private val name: String = "Campo flores"
    private var area: Area = Area.FIELDLANDS

    fun with(area: Area) = apply {
        this.area = area
    }

    fun build(): Location = Location(id, name, area)
}
