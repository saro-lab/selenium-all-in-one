package me.saro.selenium.comm

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.json.JsonMapper
import java.net.URI

class MapHelper(
    private val map: Map<String, *>
) {
    fun get(key: String): Any? = map[key]
    fun getString(key: String): String? = map[key]?.toString()
    fun getString(key: String, defaultValue: String): String = getString(key) ?: defaultValue
    fun getMap(key: String): MapHelper? = (map[key] as Map<String, *>?)?.let { MapHelper(it) }
    fun getMapList(key: String): List<MapHelper> = (map[key] as List<Map<String, *>>?)?.let { list -> list.map { MapHelper(it) } } ?: listOf()

    companion object {
        private val trMap = object : TypeReference<Map<String, *>>() {}
        private val mapper = JsonMapper()

        fun byJson(json: String): MapHelper = MapHelper(mapper.readValue(json, trMap))
        fun byURI(uri: URI): MapHelper = MapHelper(mapper.readValue(uri.toURL(), trMap))
    }
}