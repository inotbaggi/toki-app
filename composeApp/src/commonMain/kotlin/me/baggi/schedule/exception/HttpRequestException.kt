package me.baggi.schedule.exception

class HttpRequestException(statusCode: Int, message: String) : Exception(message) {
}