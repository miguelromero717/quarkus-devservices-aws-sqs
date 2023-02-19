package de.superchat.sqstest

import java.util.UUID
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/hello")
class ExampleResource {

    @Inject
    private lateinit var producer: SQSProducer

    @Inject
    private lateinit var consumer: SQSConsumer

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(
        @QueryParam(value = "payload") payload: String? = UUID.randomUUID().toString()
    ): String {
        producer.publish(
            payload = payload,
            messageGroupId = "1000"
        )
        return consumer.consume()
    }
}