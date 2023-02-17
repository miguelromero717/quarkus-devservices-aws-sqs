package de.superchat.sqstest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jboss.logging.Logger
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse
import software.amazon.awssdk.services.sqs.model.QueueAttributeName

interface SQSUrl {

    val log: Logger
        get() = Logger.getLogger(this::class.java)

    var sqs: SqsClient
    var awsOrgId: String
    var sqsEnvironmentKey: String
    var queueName: String

    fun fullQueueUrl(queueNameParam: String? = "$sqsEnvironmentKey-$queueName"): String {
        val getQueueUrlResponse: GetQueueUrlResponse = sqs.getQueueUrl(
            GetQueueUrlRequest.builder()
                .queueName(queueNameParam)
                .queueOwnerAWSAccountId(awsOrgId)
                .build()
        )
        return getQueueUrlResponse.queueUrl()
    }

    fun createFifoQueue(queueName: String) {
        sqs.createQueue(
            CreateQueueRequest.builder()
                .queueName("$sqsEnvironmentKey-$queueName")
                .attributes(
                    mapOf(
                        QueueAttributeName.CONTENT_BASED_DEDUPLICATION to "true",
                        QueueAttributeName.FIFO_QUEUE to "true"
                    )
                )
                .build()
        )
    }

    fun removeQueue(queueName: String) {
        runBlocking {
            launch(Dispatchers.IO) {
                sqs.deleteQueue(
                    DeleteQueueRequest.builder()
                        .queueUrl(fullQueueUrl(queueNameParam = queueName))
                        .build()
                )
            }.join()
        }
    }
}