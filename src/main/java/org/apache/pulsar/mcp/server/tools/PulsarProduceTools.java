package org.apache.pulsar.mcp.server.tools;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty(name = "mcp.features.pulsar-client", havingValue = "true", matchIfMissing = true)
public class PulsarProduceTools {

    private final PulsarClient pulsarClient;

    public PulsarProduceTools(PulsarClient pulsarClient) {
        this.pulsarClient = pulsarClient;
    }

    @Tool(description = "Produce a message to a Pulsar topic")
    public String produceMessage(
            @ToolParam(description = "The topic name") String topicName,
            @ToolParam(description = "The message content") String message,
            @ToolParam(description = "Optional message key") String key,
            @ToolParam(description = "Optional message properties") Map<String, String> properties
    ) {
        try (Producer<byte[]> producer = pulsarClient.newProducer()
                .topic(topicName)
                .create()) {

            var messageBuilder = producer.newMessage()
                    .value(message.getBytes());

            if (key != null && !key.trim().isEmpty()) {
                messageBuilder.key(key);
            }

            if (properties != null && !properties.isEmpty()) {
                messageBuilder.properties(properties);
            }

            var messageId = messageBuilder.send();
            return "Successfully sent message to topic: " + topicName +
                    ", MessageId: " + messageId.toString();

        } catch (PulsarClientException e) {
            return "Error producing message: " + e.getMessage();
        }
    }
}
