package com.felipecosta.microservice.server.renderer.impl

import com.mitchellbosecke.pebble.PebbleEngine
import com.mitchellbosecke.pebble.template.PebbleTemplate
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers.any
import org.mockito.Matchers.eq
import java.io.StringWriter
import kotlin.test.assertEquals

class PebbleRendererTest {

    var pebbleEngine: PebbleEngine = mock()

    var pebbleTemplate: PebbleTemplate = mock()

    lateinit var pebbleRenderer: PebbleRenderer

    @Before
    fun setUp() {
        pebbleRenderer = PebbleRenderer(pebbleEngine)
    }

    @Test
    fun givenOutputObjectWithTemplateWhenRenderThenAssertOutputText() {
        val outputObject = OutputObject("title", "content")

        whenever(pebbleEngine.getTemplate(eq("index.html"))).thenReturn(pebbleTemplate)
        whenever(pebbleTemplate.evaluate(any(StringWriter::class.java), eq(mapOf("outputobject" to outputObject)))).then {
            val stringWriter = it.arguments[0] as StringWriter
            stringWriter.append("awesome text")
        }

        val output = pebbleRenderer.render(outputObject, "index.html")

        assertEquals("awesome text", output)
    }

    @Test
    fun givenMapWithTemplateWhenRenderThenAssertOutputText() {
        val outputMap = mapOf("title" to "My First Website", "content" to "My Interesting Content")

        whenever(pebbleEngine.getTemplate(eq("index.html"))).thenReturn(pebbleTemplate)
        whenever(pebbleTemplate.evaluate(any(StringWriter::class.java), eq(outputMap))).then {
            val stringWriter = it.arguments[0] as StringWriter
            stringWriter.append("awesome text")
        }

        val output = pebbleRenderer.render(outputMap, "index.html")

        assertEquals("awesome text", output)
    }

    @Test
    fun givenEmptyMapWithTemplateWhenRenderThenAssertOutputText() {
        val outputObject = emptyMap<String, Any>()

        whenever(pebbleEngine.getTemplate(eq("index.html"))).thenReturn(pebbleTemplate)
        whenever(pebbleTemplate.evaluate(any(StringWriter::class.java))).then {
            val stringWriter = it.arguments[0] as StringWriter
            stringWriter.append("awesome text")
        }

        val output = pebbleRenderer.render(outputObject, "index.html")

        assertEquals("awesome text", output)
    }

    class OutputObject(val title: String, val content: String)

}