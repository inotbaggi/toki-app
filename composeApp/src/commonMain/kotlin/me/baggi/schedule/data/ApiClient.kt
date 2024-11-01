package me.baggi.schedule.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

object ApiClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend inline fun <reified T> get(endpoint: String): T {
        val response = client.get("https://furina.qubixmc.net/api/v1/$endpoint")
        if (response.status.isSuccess()) return response.body()
        if (response.status.value == 404) return null as T
        throw HttpRequestException(response.status.value, response.body())
    }
}
