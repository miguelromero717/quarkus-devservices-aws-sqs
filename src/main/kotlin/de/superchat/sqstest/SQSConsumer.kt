package de.superchat.sqstest

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

@ApplicationScoped
class SQSConsumer : SQSUrl {

    override var queueName: String = "my-queue.fifo"
    override var awsOrgId: String = "012345"
    override var sqsEnvironmentKey: String = "test"

    @Inject
    override lateinit var sqs: SqsClient

    fun consume(): String {
        try {
            val messageReq = ReceiveMessageRequest.builder()
                .queueUrl(fullQueueUrl())
                .build()

            val response = sqs.receiveMessage(messageReq)

            if (response.messages().isEmpty()) {
                return "No Message"
            }

            return response.messages().first().body()
        } catch (e: Exception) {
            log.error(e.message)
            throw e
        }
    }
}