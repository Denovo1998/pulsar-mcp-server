package org.apache.pulsar.mcp.server.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.TopicStats;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "mcp.features.pulsar-admin-topics", havingValue = "true", matchIfMissing = true)
public class PulsarTopicTools {
    private final PulsarAdmin pulsarAdmin;
    private final ObjectMapper objectMapper;

    public PulsarTopicTools(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
        this.objectMapper = new ObjectMapper();
    }

    @Tool(description = "List all topics in a namespace")
    public String listTopics(
            @ToolParam(description = "The namespace name in format tenant/namespace") String namespace
    ) {
        try {
            List<String> topics = pulsarAdmin.topics().getList(namespace);
            return objectMapper.writeValueAsString(topics);
        } catch (Exception e) {
            return "Error listing topics: " + e.getMessage();
        }
    }

    @Tool(description = "Create a topic with specified number of partitions")
    public String createTopic(
            @ToolParam(description = "The topic name") String topicName,
            @ToolParam(description = "Number of partitions (0 for non-partitioned)") int partitions
    ) {
        try {
            if (partitions == 0) {
                pulsarAdmin.topics().createNonPartitionedTopic(topicName);
            } else {
                pulsarAdmin.topics().createPartitionedTopic(topicName, partitions);
            }
            return "Successfully created topic: " + topicName +
                    (partitions > 0 ? " with " + partitions + " partitions" : " (non-partitioned)");
        } catch (PulsarAdminException e) {
            return "Error creating topic: " + e.getMessage();
        }
    }

    @Tool(description = "Delete a topic")
    public String deleteTopic(
            @ToolParam(description = "The topic name") String topicName,
            @ToolParam(description = "Force delete even if there are active consumers/producers") boolean force
    ) {
        try {
            pulsarAdmin.topics().delete(topicName, force);
            return "Successfully deleted topic: " + topicName;
        } catch (PulsarAdminException e) {
            return "Error deleting topic: " + e.getMessage();
        }
    }

    @Tool(description = "Get statistics for a topic")
    public String getTopicStats(
            @ToolParam(description = "The topic name") String topicName
    ) {
        try {
            TopicStats stats = pulsarAdmin.topics().getStats(topicName);
            return objectMapper.writeValueAsString(stats);
        } catch (Exception e) {
            return "Error getting topic stats: " + e.getMessage();
        }
    }

    @Tool(description = "Unload a topic from the current serving broker")
    public String unloadTopic(
            @ToolParam(description = "The topic name") String topicName
    ) {
        try {
            pulsarAdmin.topics().unload(topicName);
            return "Successfully unloaded topic: " + topicName;
        } catch (PulsarAdminException e) {
            return "Error unloading topic: " + e.getMessage();
        }
    }
}