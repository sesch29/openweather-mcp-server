package info.schefczyk.mcp.mcp_weather_server;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class McpWeatherServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mcpStdIo() {
        var stdioParams = ServerParameters.builder("java")
                .args("-jar", "/home/sebl/workspaces2/my-github/mcp-weather-server/target/mcp-weather-server-0.0.1-SNAPSHOT.jar")
                .args("-Dspring.ai.mcp.server.stdio=true")
                .build();
        var stdioTransport = new StdioClientTransport(stdioParams);
        var mcpClient = McpClient.sync(stdioTransport).build();

        System.out.println("Starting mcpClient.initialize ...");
        var init = mcpClient.initialize();
        System.out.println(init);
        System.out.println(init.serverInfo().version());
        System.out.println("Finished mcpClient.initialize.");

        McpSchema.ListToolsResult toolsList = mcpClient.listTools();
        System.out.println("toolsList: " + toolsList);

        McpSchema.CallToolResult weather = mcpClient.callTool(
                new McpSchema.CallToolRequest("getWeatherForecastByLocation",
                        Map.of("latitude", "49.640556", "longitude", "8.278889")));
        System.out.println("weather: " + weather);

        mcpClient.closeGracefully();
    }

    @Test
    void mcpClient() {

    }
}
