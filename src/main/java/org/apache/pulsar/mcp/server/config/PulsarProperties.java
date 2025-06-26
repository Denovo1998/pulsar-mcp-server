package org.apache.pulsar.mcp.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "pulsar")
@Validated
public record PulsarProperties(
        @Valid
        Admin admin,
        @Valid
        Service service
) {
    // 内部 record 的 url 字段上的 @NotBlank 是正确的
    public record Admin(@NotBlank(message = "pulsar.admin.url must not be blank") String url) {
    }

    public record Service(@NotBlank(message = "pulsar.service.url must not be blank") String url) {
    }
}