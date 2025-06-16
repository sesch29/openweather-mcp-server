# Release

## During development

- manually:
    - increment version in src/main/resources/application.yml#spring.ai.mcp.server.version
- during development the version must be snapshot: `mvn versions:set -DnewVersion=0.1.3-SNAPSHOT`

## Releasing

- `mvn release:prepare` This creates the non-snapshot jar, without dev/prod profiles.
    - If on version `0.1.3-SNAPSHOT`, during wizard set to `0.1.3` for release. Set git version tag to `v0.1.3`.
- `mvn release:perform -Darguments="-Dmaven.deploy.skip=true"` 
