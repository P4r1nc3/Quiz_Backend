# FlashDash Service - README

## Overview

FlashDash offers an innovative platform for flashcards and quizzes, enabling users to effectively study and memorize information. This README guides you through the process of setting up and running the FlashDash service locally, focusing on configuration requirements and environment setup.

## Prerequisites

Ensure the following are installed on your system:

- **Java JDK 17** or later
- **Maven** 3.6.3 or later
- **MySQL Database** (MySQL 8 recommended)

## Swagger Documentation

For API documentation and interactive testing, visit the Swagger UI:

[FlashDash Swagger UI](https://p4r1nc3.github.io/FlashDash_Backend/)

## Setup Instructions

### Step 1: Clone the Repository

Begin by cloning the FlashDash repository to your local machine:

```bash
git clone https://github.com/P4r1nc3/FlashDash_Backend
```

Then, navigate to the project directory:

```bash
cd FlashDash_Backend
```

### Step 2: Configure Environment Variables

FlashDash requires the configuration of certain environment variables related to database connectivity and JWT authentication. These are defined in the `application.properties` file and must be set prior to running the application:

- `DB_HOST`: Database URL (e.g., `jdbc:mysql://localhost:3306/flashdash_db`)
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_KEY`: Secret key for JWT token signing

### Step 3: Configuring Environment Variables in IntelliJ IDEA

To configure environment variables in IntelliJ IDEA:

1. Open the project in IntelliJ IDEA.
2. Navigate to the `Run` menu and select `Edit Configurations...`
<img width="466" alt="2" src="https://github.com/P4r1nc3/FlashDash_Backend/assets/51295585/659c492d-f95f-4799-b3c9-25d4e9f6da05">

3. In the `Run/Debug Configurations` window, select your Spring Boot application.
<img width="1152" alt="3" src="https://github.com/P4r1nc3/FlashDash_Backend/assets/51295585/c97a9b93-fd88-4df7-bb22-2f865183ad4f">

4. Click `Modify options`.
<img width="1152" alt="4" src="https://github.com/P4r1nc3/FlashDash_Backend/assets/51295585/d9abc578-ba92-4923-9691-ea61a562e6cc">

5. From the `Operating System` section, select `Environment variables.`
<img width="503" alt="5" src="https://github.com/P4r1nc3/FlashDash_Backend/assets/51295585/c327aad3-6221-48cc-8b08-c89819e769da">

6. In the `Environment variables` input field, enter your variables as key-value pairs, separated by semicolons:
```
JWT_KEY=sQF1C+qQJpmV9A/M8/LPd5esayq2dpG/cD0MCEwJZjM;DB_HOST=jdbc:mysql://localhost:3306/flash-dash?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false;DB_USERNAME=root;DB_PASSWORD=admin12345
```

<img width="1152" alt="6" src="https://github.com/P4r1nc3/FlashDash_Backend/assets/51295585/9741cdb8-509a-4524-adce-c8f5024e8a98">

7. Click `Apply` and then `OK` to save the configuration.

### Step 4: Building the Application

To build the FlashDash application, use the following Maven command:

```bash
mvn clean install
```

This command compiles the project and runs any tests, ensuring the application is correctly set up.

### Step 5: Running the Application

After successfully building the project, run it with Maven by executing:

```bash
mvn spring-boot:run
```

This starts the FlashDash service, making it accessible at `http://localhost:8080`.

## Conclusion

Following these instructions, you have successfully set up and launched the FlashDash service on your local machine. The service is now ready to facilitate the creation, management, and utilization of flashcards and quizzes.
