package org.apache.pulsar.mcp.server.config;

import org.apache.pulsar.mcp.server.tools.PulsarProduceTools;
import org.apache.pulsar.mcp.server.tools.PulsarTenantTools;
import org.apache.pulsar.mcp.server.tools.PulsarTopicTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarMcpToolConfiguration {
    @Bean
    public ToolCallbackProvider pulsarAdminTools(
            PulsarTenantTools tenantTools,
            PulsarTopicTools topicTools
    ) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(
                        tenantTools,
                        topicTools
                )
                .build();
    }

    @Bean
    public ToolCallbackProvider pulsarClientTools(
            PulsarProduceTools produceTools
    ) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(produceTools)
                .build();
    }
}
