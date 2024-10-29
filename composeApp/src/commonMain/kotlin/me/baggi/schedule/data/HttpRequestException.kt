package me.baggi.schedule.data

class HttpRequestException(statusCode: Int, message: String) : Exception(message) {
}