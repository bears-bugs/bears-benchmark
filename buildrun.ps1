# Builds and runs application (in Windows Powershell environment - recommend running this in Docker CLI window)
# To run, first set session's permission
#   Set-ExecutionPolicy -Scope CurrentUser Unrestricted

# Spring Container
./mvnw install dockerfile:build

# Front end
docker build -t cscie599/frontend frontend

# Start it up
docker-compose up
