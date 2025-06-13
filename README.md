# Readme

## End-user Usage (nerd-level)

If you just want to use it in your MCP-ready AI application of your choice, follow these steps:

- Get your personal openweathermap.org API key:
    - Create an [Account](https://home.openweathermap.org/users/sign_up)
    - Create an API-key [here](https://home.openweathermap.org/api_keys)
- Open the configuration file of your AI application. Depending on the app, this might be different. You need to add
  this configuration:
    ```yml
    mcpServers:
      openweather-mcp-server:
        type: stdio
        command: java
        args:
          - -Dlogging.file.name="/app/my-mcp-servers/openweather-mcp-server.log"
          - -Dsecret.openweathermap.apikey=YOUR-OPENWEATHERMAP-API-KEY
          - -jar /app/my-mcp-servers/openweather-mcp-server-0.1.0.jar
    ```
- Download the *.jar file of the mcp-server
- Copy it into a location that is reachable
    - LibreChat in Docker: create a folder `/LIBRECHAT/my-mcp-servers/`
- Make sure, `java` is installed in the location
    - LibreChat in Docker: in `/LIBRECHAT/Dockerfile` change this line:

      ```Dockerfile
      # original
      RUN apk add --no-cache python3 py3-pip uv
      # change into this
      RUN apk add --no-cache python3 py3-pip uv openjdk21-jre
      ```

- Make sure, everything is reachable and packed together:
    - LibreChat in Docker:
        - In file `/LIBRECHAT/docker-compose.yml`:

            ```yml
            # original
            image: ghcr.io/danny-avila/librechat-dev:latest
            # change into this
            # image: ghcr.io/danny-avila/librechat-dev:latest
            build: .
            ```

        - Now you can rebuild

- Now you can rebuild, restart. With the current versions it is sufficient to execute
  `docker compose build --no-cache && docker compose up -d`. On any problems, please stick
  to the [update guide](https://www.librechat.ai/docs/local/docker#update-librechat).
- In your AI application, you should now be able to activate the "openweather-mcp-server" and ask for the current
  weather in
  any city :-)

## Development: Environment variables

To execute the remote calls to openweathermap.org, you need a personal API key (see above) and set it in an application
profile (`application-dev.yml`) or environment-variable.

Set this environment variable (e.g. in your test-starters):
`secret.openweathermap.apikey=YOUR-OPENWEATHERMAP-API-KEY`
