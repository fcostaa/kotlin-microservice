package com.felipecosta.microservice.app.movies.frontcontroller

import com.felipecosta.microservice.app.NotImplementedRequest
import com.felipecosta.microservice.app.core.domain.MoviesRepository
import com.felipecosta.microservice.app.core.domain.entity.Movie
import com.felipecosta.microservice.server.Request
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class UpdateMovieFrontCommandTest {
    val mockMoviesRepository: MoviesRepository = mock()

    val frontCommand = UpdateMovieFrontCommand().apply {
        moviesRepository = mockMoviesRepository
    }

    @Test
    fun givenJsonBodyThenReturnUpdatedMovie() {
        frontCommand.init(object : Request by NotImplementedRequest() {
            override val body: String = """{"name":"New movie"}"""

            override val routeParams = mapOf(":id" to "1")
        })

        whenever(mockMoviesRepository.find(eq(1))).thenReturn(Movie("Old movie", 1))

        frontCommand.process()

        kotlin.test.assertEquals("""{"response":{"name":"New movie","id":1}}""", frontCommand.output)
    }
}