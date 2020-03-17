package data

sealed class Response<out T> {
    class RequestSuccess<out T>(val data:T):Response<T>()
    class RequestError(val exception: Exception): Response<Nothing>()
}