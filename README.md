# Pulsar MCP Server (Java)

A Java implementation of Pulsar MCP Server using Spring Boot and Spring AI MCP Server Boot Starter.

## Features

- **Pulsar Admin Tools**: Complete Pulsar administrative operations
    - Tenant management (list, create, update, delete)
    - Namespace management
    - Topic management
    - Broker management
    - Cluster management
    - Function management

- **Pulsar Client Tools**: Message production and consumption
    - Message production with properties and keys
    - Message consumption with various subscription types

- **Security Profiles**:
    - Read-only mode (default)
    - Write mode (enabled with --spring.profiles.active=write)

- **Feature Flags**: Enable/disable specific tool groups via configuration

## Quick Start

### Prerequisites

- Java 17+
- Apache Pulsar cluster running
- Maven 3.6+

### Build

```bash
mvn clean package -DskipTests
