package com.felipecosta.microservice.server.frontcontroller.impl

import com.beust.klaxon.*
import com.felipecosta.microservice.server.Request
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream
import org.junit.Before
import org.junit.Test
import spark.Response
import kotlin.test.assertEquals

class JsonSparkFrontCommandTest {

    val request: Request = mock()

    val response: Response = mock()

    lateinit var jsonSparkFrontCommand: JsonSparkFrontCommand

    @Before
    fun setUp() {
        jsonSparkFrontCommand = JsonSparkFrontCommand()
        jsonSparkFrontCommand.init(request, response)
    }

    @Test
    fun whenProcessThenAssertResponseBodyContainsUrl() {
        whenever(request.url).thenReturn("http://127.0.0.1:4567/hello-front-controller-json")

        jsonSparkFrontCommand.process()

        val responseBody = parse(jsonSparkFrontCommand.output)
        assertEquals("http://127.0.0.1:4567/hello-front-controller-json", responseBody.string("url"))
    }

    @Test
    fun whenProcessThenAssertResponseBodyContainsHost() {
        whenever(request.host).thenReturn("127.0.0.1:4567")

        jsonSparkFrontCommand.process()

        val responseBody = parse(jsonSparkFrontCommand.output)
        assertEquals("127.0.0.1:4567", responseBody.string("host"))
    }

    @Test
    fun whenProcessThenAssertResponseBodyContainsUserAgent() {
        whenever(request.userAgent).thenReturn("Mozilla/5.0")

        jsonSparkFrontCommand.process()

        val responseBody = parse(jsonSparkFrontCommand.output)
        assertEquals("Mozilla/5.0", responseBody.string("user-agent"))
    }

    @Test
    fun GivenOneQueryParamWithOneParameterWhenProcessThenAssertResponseBodyContainsQueryParams() {
        whenever(request.params).thenReturn(mapOf("q" to arrayOf("search")))

        jsonSparkFrontCommand.process()

        val responseBody = parse(jsonSparkFrontCommand.output)
        val searchQuery = responseBody.array<com.beust.klaxon.JsonObject>("query-params")!![0]["q"] as JsonArray<*>
        assertEquals(listOf("search"), searchQuery.toList())
    }

    @Test
    fun GivenOneQueryParamWithTwoParameterWhenProcessThenAssertResponseBodyContainsQueryParams() {
        whenever(request.params).thenReturn(mapOf("q" to arrayOf("search", "awesome search")))

        jsonSparkFrontCommand.process()

        val responseBody = parse(jsonSparkFrontCommand.output)
        val searchQuery = responseBody.array<com.beust.klaxon.JsonObject>("query-params")!![0]["q"] as JsonArray<*>
        assertEquals(listOf("search", "awesome search"), searchQuery.toList())
    }

    private fun parse(json: String?): JsonObject {
        val byteArray = json!!.toByteArray()
        val inputStream = ByteInputStream(byteArray, byteArray.size)
        return Parser().parse(inputStream)!! as JsonObject
    }

}