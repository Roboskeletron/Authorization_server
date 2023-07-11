# Authorization Server

The Authorization Server is a component of the application responsible for managing authentication, authorization, and issuing JSON Web Tokens (JWT) to client applications. It connects to the client and user databases to authenticate users and generate tokens for secure access to the application's APIs.

## Features

- User Management: The Authorization Server provides endpoints to manage user accounts. Users can be created, updated, and retrieved.
- Client Management: Administrators can manage client applications. Clients are registered in the system and assigned specific authorization scopes.
- Token Generation: Upon successful authentication, the Authorization Server generates JWTs, which can be used by client applications to access protected APIs.
- Role-Based Access Control: Users with administrative authorities have the ability to manage client applications and user accounts. Super_users have additional privileges.

## Endpoints

The Authorization Server exposes the following standard OAuth 2.0 endpoints:

- `/oauth/authorize`: Endpoint for user authorization.
- `/oauth/token`: Endpoint to obtain access and refresh tokens.
- `/oauth/check_token`: Endpoint to validate the authenticity of a JWT.
- `/oauth/token_key`: Endpoint to retrieve the public key for token verification.

## Prerequisites

- Java 17 or higher
- Spring Boot framework
- PostgreSQL database for storing client and user information

## Getting Started

1. Clone the repository.
2. Configure the database connection in the `application.yml` file.
3. Build the project using Gradle:
   ```
   ./gradlew build
   ```
4. Build a Docker image of the Authorization Server:
   ```
   ./gradlew bootBuildImage --imageName=roboskeletron/authserver
   ```
5. Run the Docker image:
   ```
   docker run -p 8080:8080 roboskeletron/authserver
   ```
6. Access the Authorization Server API endpoints using the provided API documentation or client application.

## Usage

1. The Authorization Server automatically creates a default super user and a default client on startup. These entities cannot be deleted but can have their properties modified.
2. Use the provided API endpoints to manage user accounts and client applications.
3. Additional users and clients can be created through the appropriate endpoints.
4. Once a client application is registered, obtain client credentials (client_id and client_secret) for authentication.
5. Use the obtained credentials to authenticate the client application and obtain an access token.
6. Use the access token to make authenticated requests to the protected APIs.

## Security

The Authorization Server uses HTTP for simplicity. For production deployment, it is highly recommended to secure the communication using HTTPS. This can be achieved by configuring a reverse proxy or load balancer with SSL termination in front of the Authorization Server.

## Contributing

Contributions to the Authorization Server are welcome! If you find any issues or would like to suggest enhancements, please submit a pull request or open an issue in the repository.

## License

This project is licensed under the [MIT License](LICENSE).