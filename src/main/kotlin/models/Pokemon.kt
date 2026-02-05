package dev.gemmabcr.models

data class Pokemon(
    val hisuiId: Int,
    val generalId: Int,
    val name: String,
    val types: List<Type>,
    val location: List<Location>,
    val toDos: List<ToDo>,
    val specialCondition: SpecialCondition?,
) {
    private fun matchName(name: String?): Boolean = when(name.isNullOrBlank()) {
        true -> false
        false -> this.name.lowercase().contains(name!!)
    }

    private fun isNumber(number: Int?): Boolean = this.hisuiId == number

    private fun matchArea(area: Area?): Boolean = location.any { it.area == area }

    fun match(criteria: QueryCriteria): Boolean =
        matchName(criteria.name) || isNumber(criteria.number) || matchArea(criteria.area)
}
