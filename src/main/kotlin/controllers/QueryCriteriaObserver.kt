package dev.gemmabcr.controllers

import dev.gemmabcr.models.QueryCriteria

class QueryCriteriaObserver {
    private lateinit var subscriber: (QueryCriteria)-> Unit
    fun subscribe(subscriber: (QueryCriteria)-> Unit) {
        this.subscriber = subscriber
    }

    fun notify(criteria: QueryCriteria) {
        subscriber(criteria)
    }
}
