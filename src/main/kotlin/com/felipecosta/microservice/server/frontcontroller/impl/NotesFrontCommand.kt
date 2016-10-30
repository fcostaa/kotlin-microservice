package com.felipecosta.microservice.server.frontcontroller.impl

import com.felipecosta.microservice.server.frontcontroller.SparkFrontCommand
import spark.Request
import spark.Response

class NotesFrontCommand(request: Request, response: Response) : SparkFrontCommand(request, response) {
    override fun process() {
        render(Output("My First Website", "My Interesting Content"), "views/notes.html")
    }

    data class Output(val title: String, val content: String) {

    }
}