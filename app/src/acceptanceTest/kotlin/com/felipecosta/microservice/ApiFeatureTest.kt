package com.felipecosta.microservice

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import cucumber.api.java8.En
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApiFeatureTest(apiClient: ApiClient) : En {

    init {
        lateinit var postedMovie: JsonObject
        lateinit var moviesResponse: JsonArray<JsonObject>

        Given("""^the movie "(.*)" exists$""") { movieName: String ->
            postedMovie = apiClient.postMovie(movieName)
        }

        When("""^the user makes a request for movies$""") {
            moviesResponse = apiClient.getMovies()
        }

        Then("""^the movie "(.*)" are shown on response$""") { movieName: String ->
            assertTrue { moviesResponse.asSequence().map { it["name"] }.contains(movieName) }
        }


        lateinit var insertedMovie: JsonObject

        When("""^insert movie with title "(.*)"$""") { movieName: String ->
            insertedMovie = apiClient.postMovie(movieName)
        }

        Then("""^the response should contains an id$""") {
            assertTrue { insertedMovie["id"] as Int > 0 }
        }


        lateinit var editedMovie: JsonObject

        When("""^the user changes this movie name to "(.*)"$""") { newMovieName: String ->
            editedMovie = apiClient.putMovie(postedMovie["id"] as Int, newMovieName)
        }

        Then("""^the movie name changed to "(.*)"$""") { editedMovieName: String ->
            assertEquals(editedMovieName, editedMovie["name"])
        }
    }
}