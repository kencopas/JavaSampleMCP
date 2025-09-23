package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static DataClient dataClient = new DataClient();

    public static void main(String[] args) {

        // Stdio Server Transport
        var transportProvider = new StdioServerTransportProvider(new ObjectMapper());

        // Sync Tool Specification
        var syncToolSpecification = getSyncToolSpecification();

        McpServer.sync(transportProvider)
                .serverInfo("javaone-mcp-server", "0.0.1")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .tools(true)
                        .logging()
                        .build())
                .tools(syncToolSpecification)
                .build();

        log.info("Starting server...");
    }

    private static McpServerFeatures.SyncToolSpecification[] getSyncToolSpecification() {

        var schema = """
                {
                    "type": "object",
                    "id": "urn:jsonschema:Operation",
                    "properties": {
                        "operation": {
                            "type": "string"
                        }
                    }
                }
                """;

        McpServerFeatures.SyncToolSpecification[] syncToolSpecifications = {
            // Each Sync Tool Specification is an MCP Tool in the format (Definition, Implementation)
            new McpServerFeatures.SyncToolSpecification(
                // Tool  Definition
                new McpSchema.Tool("get_user_info", "Get the user's general information.", schema),
                // Tool Implementation
                (McpSyncServerExchange exchange, Map<String, Object> arguments) -> {
                    List<McpSchema.Content> contents = new ArrayList<>() {};
                    String userInfo = dataClient.getUserInfo();
                    // Every object returned from an MCP Tool must be wrapped in a Content object
                    contents.add(new McpSchema.TextContent(userInfo));
                    // An MCP Tool should return a list of Content objects wrapped in a CallToolResult
                    return new McpSchema.CallToolResult(contents, false);
                }
            )
        };

        return syncToolSpecifications;
    }
}