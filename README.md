## Getting Started
Use Windows
1. .\gradlew build
2. .\gradlew bootRun

### To run with persisted DB locally
Note: this is a future feature that is not yet working.
The application-local.properties has been created but there is a problem creating the Repositories.

.\gradlew bootRun --args='--spring.profiles.active=local'