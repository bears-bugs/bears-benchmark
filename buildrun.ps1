# Builds and runs application (in Windows Powershell environment - recommend running this in Docker CLI window)
# To run, first set session's permission
#   Set-ExecutionPolicy -Scope CurrentUser Unrestricted
docker run -e POSTGRES_PASSWORD=root -e POSTGRES_USER=postgres -e POSTGRES_DB=H2MSDatabase -d -p 5432:5432 --name postgres postgres
./mvnw install dockerfile:build
docker stop postgres
docker rm postgres
docker-compose up