package de.superchat.sqstest

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/hello")
class ExampleResource {

    @Inject
    private lateinit var producer: SQSProducer

    @Inject
    private lateinit var consumer: SQSConsumer

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        producer.publish()
        consumer.consume()
        return "Hello RESTEasy"
    }
}