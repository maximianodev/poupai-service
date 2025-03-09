## Financial Auth

This project is a financial authentication service developed with Spring Boot.

### Features

- User registration
- User login
- JWT token generation
- Password recovery

### Technologies

- Java
- Spring Boot
- Maven
- JPA
- BCrypt

### How to Run

[How to start database](/docs/database.md)

1. Clone the repository:
   ```sh
   git clone git@github.com:maximianodev/financial-auth.git
   ```
2. Navigate to the project directory:
   ```sh
   cd financial-auth
   ```
3. Run the project:
   ```sh
   mvn spring-boot:run
   ```
   
### Configuration

Ensure to set the following environment variables for the application to run properly:
- `MYSQL_URL` \<!-- The URL of the MySQL database -->
- `MYSQL_USER` \<!-- The username for the MySQL database -->
- `MYSQL_PASSWORD` \<!-- The password for the MySQL database -->
- `JWT_SECRET` \<!-- The secret key used for generating JWT tokens -->
- `APP_URL` \<!-- The base URL of the application -->
- `SMTP_EMAIL` \<!-- The email address used for sending SMTP emails -->
- `SMTP_PASSWORD` \<!-- The password for the SMTP email account -->
