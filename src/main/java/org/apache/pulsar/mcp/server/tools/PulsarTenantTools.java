package org.apache.pulsar.mcp.server.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.common.policies.data.TenantInfo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@ConditionalOnProperty(name = "mcp.features.pulsar-admin-tenants", havingValue = "true", matchIfMissing = true)
public class PulsarTenantTools {
    private final PulsarAdmin pulsarAdmin;
    private final ObjectMapper objectMapper;

    public PulsarTenantTools(PulsarAdmin pulsarAdmin) {
        this.pulsarAdmin = pulsarAdmin;
        this.objectMapper = new ObjectMapper();
    }

    @Tool(description = "List all tenants in the Pulsar cluster")
    public String listTenants() {
        try {
            List<String> tenants = pulsarAdmin.tenants().getTenants();
            return objectMapper.writeValueAsString(tenants);
        } catch (Exception e) {
            return "Error listing tenants: " + e.getMessage();
        }
    }

    @Tool(description = "Get configuration details for a specific tenant")
    public String getTenant(
            @ToolParam(description = "The tenant name") String tenant
    ) {
        try {
            TenantInfo tenantInfo = pulsarAdmin.tenants().getTenantInfo(tenant);
            return objectMapper.writeValueAsString(tenantInfo);
        } catch (PulsarAdminException e) {
            return "Error getting tenant info: " + e.getMessage();
        } catch (Exception e) {
            return "Error serializing tenant info: " + e.getMessage();
        }
    }

    @Tool(description = "Create a new tenant with specified configuration")
    public String createTenant(
            @ToolParam(description = "The tenant name") String tenant,
            @ToolParam(description = "List of admin roles") List<String> adminRoles,
            @ToolParam(description = "List of allowed clusters") Set<String> allowedClusters
    ) {
        try {
            TenantInfo tenantInfo = TenantInfo.builder()
                    .adminRoles(Set.copyOf(adminRoles))
                    .allowedClusters(allowedClusters)
                    .build();

            pulsarAdmin.tenants().createTenant(tenant, tenantInfo);
            return "Successfully created tenant: " + tenant;
        } catch (PulsarAdminException e) {
            return "Error creating tenant: " + e.getMessage();
        }
    }

    @Tool(description = "Update configuration for an existing tenant")
    public String updateTenant(
            @ToolParam(description = "The tenant name") String tenant,
            @ToolParam(description = "List of admin roles") List<String> adminRoles,
            @ToolParam(description = "List of allowed clusters") Set<String> allowedClusters
    ) {
        try {
            TenantInfo tenantInfo = TenantInfo.builder()
                    .adminRoles(Set.copyOf(adminRoles))
                    .allowedClusters(allowedClusters)
                    .build();

            pulsarAdmin.tenants().updateTenant(tenant, tenantInfo);
            return "Successfully updated tenant: " + tenant;
        } catch (PulsarAdminException e) {
            return "Error updating tenant: " + e.getMessage();
        }
    }

    @Tool(description = "Delete an existing tenant")
    public String deleteTenant(
            @ToolParam(description = "The tenant name") String tenant
    ) {
        try {
            pulsarAdmin.tenants().deleteTenant(tenant);
            return "Successfully deleted tenant: " + tenant;
        } catch (PulsarAdminException e) {
            return "Error deleting tenant: " + e.getMessage();
        }
    }
}
