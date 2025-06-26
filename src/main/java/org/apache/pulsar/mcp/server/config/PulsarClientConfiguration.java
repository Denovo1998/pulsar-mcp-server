package org.apache.pulsar.mcp.server.config;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarClientConfiguration {

    @Bean
    public PulsarAdmin pulsarAdmin(PulsarProperties properties) throws PulsarClientException {
        return PulsarAdmin.builder()
                .serviceHttpUrl(properties.admin().url())
                .build();
    }

    @Bean
    public PulsarClient pulsarClient(PulsarProperties properties) throws PulsarClientException {
        return PulsarClient.builder()
                .serviceUrl(properties.service().url())
                .build();
    }
}
