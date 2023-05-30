# Documentation 
# API - Fivvy Backend Challenge

## Description
This is an API developed for the Fivvy Backend Challenge. The purpose of this API is to manage the acceptance of terms and conditions.

## Technologies
    - Java 11
    - Spring Boot 2.7.4
    - Maven
    - AWS DynamoDbB
    - Docker
    - JUnit
    - Mockito

## Instructions
![image](https://github.com/francomiro/Fivvy-Backend-Challenge/assets/38414853/1b03bba0-084c-4fd0-b744-00ceedab611b)

## Run with Docker
To run the API using Docker, follow the steps below:

   1. Make sure you have Docker installed on your system.
   2. Clone the repository to your local computer.
   3. Navigate to the root directory of the project and open the console.
   4. Build the docker image by running the following command
    
    docker build -t fivvy-backend-challenge .
    
   5. Start a Docker container based on the created image
   
    docker run -p 8080:8080 fivvy-backend-challenge
   
   6. Now you can access the API in your browser or use Postman tools to interact with it.
   7. The API base URL will be:
        
    http://localhost:8080
    
    
   To stop the container, you can run the following command in another terminal:
    
    docker stop $(docker ps -aq --filter ancestor=fivvy-backend-challenge)

## Endpoints
#### List Disclaimers

- Method: GET
- URL: `http://localhost:8080/disclaimer/`
- Request Param:
  - `text` (optional): Allows you to filter by Disclaimers whose `text` ttribute contains or is the same as the `text` parameter entered in the request.
- Description: This endpoint lists all available disclaimers. Optionally, the `text` parameter can be provided to filter disclaimers that contain the text specified within their `text` attribute.
- Request Examples:
        
        GET http://localhost:8080/disclaimer/?text=contract
        
        GET http://localhost:8080/disclaimer/
 
 #### Create Disclaimer

- Method: POST
- URL: `http://localhost:8080/disclaimer/`
- Request body: JSON
- Description: This endpoint creates a new disclaimer with the data provided in the request body.
- Request Examples:

{
    "name":"nombre prueba 123131231231231231231",
    "text":"prueba 1 sin id en el request",
    "version":"1.0.0"
}














