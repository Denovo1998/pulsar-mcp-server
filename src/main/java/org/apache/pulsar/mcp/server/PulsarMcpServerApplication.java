package org.apache.pulsar.mcp.server;

import org.apache.pulsar.mcp.server.config.PulsarProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(PulsarProperties.class)
@EnableScheduling
public class PulsarMcpServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PulsarMcpServerApplication.class, args);
    }
}
