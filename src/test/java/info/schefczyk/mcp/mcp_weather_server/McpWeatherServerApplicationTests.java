package info.schefczyk.mcp.mcp_weather_server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class McpWeatherServerApplicationTests {

    @Disabled("This needs a static, magic string of a previously build jar. Only useful in sequential testing.")
    @Test
    void mcpStdIo() {
        var stdioParams = ServerParameters.builder("java")
                .args("-jar", "target/openweather-mcp-server-0.1.2-SNAPSHOT.jar")
                .build();
        var stdioTransport = new StdioClientTransport(stdioParams);
        var mcpClient = McpClient.sync(stdioTransport)
                .loggingConsumer(message -> {
                    System.out.println(">> Client Logging: " + message);
                })
                .build();

        var init = mcpClient.initialize();
        System.out.println("capabilities: " + init.capabilities());
        System.out.println("serverInfo: " + init.serverInfo());
        System.out.println("instructions: " + init.instructions());
        System.out.println("protocolVersion: " + init.protocolVersion());
        mcpClient.ping();

        McpSchema.ListToolsResult toolsList = mcpClient.listTools();
        System.out.println("toolsList: " + toolsList);

        McpSchema.CallToolResult weather = mcpClient.callTool(
                new McpSchema.CallToolRequest("getWeatherForecastByLocation",
                        Map.of("latitude", "49.640556", "longitude", "8.278889")));
        System.out.println("weather: " + weather);

        assertTrue(weather.toString().contains("Mörstadt"));

        mcpClient.closeGracefully();
    }

}
