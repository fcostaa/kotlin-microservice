package com.felipecosta.microservice.app.movies.frontcontroller

import com.felipecosta.microservice.app.NotImplementedRequest
import com.felipecosta.microservice.app.core.domain.MoviesRepository
import com.felipecosta.microservice.app.core.domain.entity.Movie
import com.felipecosta.microservice.server.Request
import com.felipecosta.microservice.server.Response
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class DeleteMovieFrontCommandTest {
    private val mockMoviesRepository = mockk<MoviesRepository>()

    private val frontCommand = DeleteMovieFrontCommand(mockMoviesRepository)

    @BeforeEach
    fun setUp() {
        clearMocks(mockMoviesRepository)
    }

    @Test
    fun givenMovieIdThenAssertResponseOk() {
        frontCommand.init(object : Request by NotImplementedRequest() {
            override val routeParams = mapOf(":id" to "1")
        })

        val movie = Movie("Any movie", 1)

        every { mockMoviesRepository.find(1) } returns movie
        every { mockMoviesRepository.delete(movie) } just Runs

        frontCommand.process()

        assertEquals(Response("", 200), frontCommand.response)
    }

    @Test
    fun givenMovieIdThenVerifyDeletedOnRepository() {
        frontCommand.init(object : Request by NotImplementedRequest() {
            override val routeParams = mapOf(":id" to "1")
        })

        val movie = Movie("Any movie", 1)

        every { mockMoviesRepository.find(1) } returns movie
        every { mockMoviesRepository.delete(movie) } just Runs

        frontCommand.process()

        verify {
            mockMoviesRepository.delete(movie)
        }
    }
}