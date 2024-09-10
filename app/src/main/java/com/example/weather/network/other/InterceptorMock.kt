package com.example.weather.network.other

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.BufferedReader
import java.io.InputStream


class InterceptorMock : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
        val path = "res/raw/${url.getLastPathSegment()}"
        val response = path.readFile(this)

        require(!response.isNullOrEmpty()) { "JSON file $path should exist and not be empty" }

        return Response.Builder()
            .code(200)
            .message(response)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(response.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}

/**
 * Helpers
 */
fun String.openStream(clz: Any) = clz.javaClass.classLoader?.getResourceAsStream(this)

fun InputStream.readFile() = this.bufferedReader().use(BufferedReader::readText)

fun String.readFile(clz: Any) = this.openStream(clz)?.readFile()

fun HttpUrl.getLastPathSegment(): String = this.pathSegments.last()