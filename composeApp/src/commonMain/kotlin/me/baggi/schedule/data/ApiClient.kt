package me.baggi.schedule.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

object ApiClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend inline fun <reified T> get(endpoint: String): T {
        return client.get("https://e6ce-94-131-15-75.ngrok-free.app/api/v1/$endpoint"){
            headers["ngrok-skip-browser-warning"] = ""
        }.body()
    }
}
