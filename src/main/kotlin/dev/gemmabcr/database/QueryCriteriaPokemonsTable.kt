package dev.gemmabcr.database

import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.models.QueryCriteria
import org.jetbrains.exposed.sql.ResultRow

object QueryCriteriaPokemonsTable {
    fun filter(
        row: ResultRow,
        criteria: QueryCriteria,
        filterLocationIds: List<Int>?,
        teamIds: List<Int>?
    ): Boolean {
        if (criteria.isFiltered().not()) {
            return true
        }
        return matchNumber(row, criteria) &&
                matchName(row, criteria) &&
                matchArea(row, filterLocationIds) &&
                matchType(row, criteria) &&
                matchToDo(row, criteria) &&
                matchTeam(row, teamIds)
    }

    private fun matchNumber(row: ResultRow, criteria: QueryCriteria): Boolean =
        criteria.number?.let { row[PokemonsTable.id] == it } ?: true

    private fun matchName(row: ResultRow, criteria: QueryCriteria): Boolean =
        criteria.name?.let { row[PokemonsTable.name].lowercase().contains(it.lowercase()) } ?: true

    private fun matchArea(row: ResultRow, filterLocationIds: List<Int>?): Boolean =
        filterLocationIds?.let { locationIds -> row[PokemonsTable.locations].any { it in locationIds } } ?: true

    private fun matchType(row: ResultRow, criteria: QueryCriteria): Boolean =
        criteria.type?.let { row[PokemonsTable.types].contains(it) } ?: true

    private fun matchToDo(row: ResultRow, criteria: QueryCriteria): Boolean =
        criteria.toDo?.let { row[PokemonsTable.toDos].containsKey(it.id.toString()) } ?: true

    private fun matchTeam(row: ResultRow, teamIds: List<Int>?): Boolean =
        teamIds?.let { row[PokemonsTable.id] in it } ?: true
}
