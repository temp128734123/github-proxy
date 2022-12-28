# About
This project is a Spring Boot based service acting as a proxy to underlying GitHub's API, simplifying returned data's representation.
# Base tech
* Java 17
* Spring Boot 3.0.1
* Maven
# Containerization (docker)
To run the application in docker:
* Build the image: `docker build -t github-proxy .`
* Run a container: `docker run -p 8080:8080 github-proxy`
# API doc
Having run the application, you can access Swagger via default `http://localhost:8080/swagger-ui/index.html`
# Testing approach
Black-box testing is used to simplify and encourage refactoring. All assertions are carried out on the level of API contract. No mocking is used for GitHub API, all requests are being passed to the actual one, for accounts that have been prepared beforehand.
