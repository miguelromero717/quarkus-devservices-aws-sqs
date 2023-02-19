package de.superchat.sqstest

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@QuarkusTest
class ExampleResourceTest {

    @Test
    fun testHelloEndpoint() {
        val result = given()
            .`when`().get("/hello?payload=Miguel")
            .then()
            .statusCode(200)
            .extract().body().asString()

        assertTrue(result.contains("Miguel"))
    }

}