package com.example.retrofit2practice.others

data class Response<out T>(var status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Response<T> {
            return Response(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): Response<T> {
            return Response(Status.ERROR, null, msg)
        }

        fun <T> loading(): Response<T> {
            return Response(Status.LOADING, null, null)
        }
    }
}

enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}