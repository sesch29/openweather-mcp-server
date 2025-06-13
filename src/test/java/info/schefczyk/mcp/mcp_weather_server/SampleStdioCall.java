package info.schefczyk.mcp.mcp_weather_server;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Map;
import java.util.stream.Stream;

public class SampleStdioCall {

    public static void main(String[] args) {
        var stdioParams = ServerParameters.builder("java")
                .args("-jar", "target/openweather-mcp-server-0.1.1-SNAPSHOT.jar")
                .build();
        var stdioTransport = new StdioClientTransport(stdioParams);
        var mcpClient = McpClient.sync(stdioTransport).build();

        // 1) initialize:
        var init = mcpClient.initialize();
        mcpClient.ping();
        System.out.println(Stream.generate(() -> "#").limit(60).reduce("", String::concat));
        System.out.println("capabilities: " + init.capabilities());
        System.out.println("serverInfo: " + init.serverInfo());
        System.out.println("instructions: " + init.instructions());
        System.out.println("protocolVersion: " + init.protocolVersion());

        // 2) mcpClient:
        System.out.println(Stream.generate(() -> "#").limit(60).reduce("", String::concat));
        McpSchema.ListToolsResult toolsList = mcpClient.listTools();
        toolsList.tools().forEach(tool -> {
            System.out.println("Tool: " + tool.name() + ", description: " + tool.description() + ", schema: "
                    + tool.inputSchema());
        });

        // 3) Call the tools:
        System.out.println(Stream.generate(() -> "#").limit(60).reduce("", String::concat));
        McpSchema.CallToolResult weather = mcpClient.callTool(
                new McpSchema.CallToolRequest("getWeatherForecastByLocation",
                        Map.of("latitude", "49.640556", "longitude", "8.278889")));
        System.out.println("weather: " + weather);

        McpSchema.CallToolResult location = mcpClient.callTool(
                new McpSchema.CallToolRequest("getLocation",
                        Map.of("getLocation", "Worms, Rhineland-Palatinate")));
        System.out.println("location: " + location);

        McpSchema.CallToolResult searchLocationResult = mcpClient.callTool(
                new McpSchema.CallToolRequest("searchLocation",
                        Map.of("searchQuery", "Worms, Germany")));
        searchLocationResult.content().forEach(content -> {
            System.out.println("searchLocationResult content: " + content);
        });

        mcpClient.closeGracefully();
    }
}

