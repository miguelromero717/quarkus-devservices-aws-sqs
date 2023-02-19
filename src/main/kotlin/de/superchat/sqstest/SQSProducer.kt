package de.superchat.sqstest

import java.util.UUID
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@ApplicationScoped
class SQSProducer : SQSUrl {

    override var queueName: String = "my-queue.fifo"
    override var awsOrgId: String = "012345"
    override var sqsEnvironmentKey: String = "test"

    @Inject
    override lateinit var sqs: SqsClient

    @PostConstruct
    fun initQueue() {
        createFifoQueue(queueName = queueName)
    }

    @PreDestroy
    fun tearDown() {
        removeQueue(queueName = queueName)
    }

    fun publish(
        payload: String? = UUID.randomUUID().toString(),
        messageGroupId: String? = UUID.randomUUID().toString()
    ) {
        try {
            val message = SendMessageRequest.builder()
                .queueUrl(fullQueueUrl())
                .messageBody(payload)
                .messageGroupId(messageGroupId)
                .build()
            sqs.sendMessage(message)
        } catch (e: Exception) {
            log.error(e.message)
        }
    }
}