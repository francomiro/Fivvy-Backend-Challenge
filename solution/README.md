# Documentation 
# API - Fivvy Backend Challenge

## Description
This is an API developed for the Fivvy Backend Challenge. The purpose of this API is to manage the acceptance of terms and conditions.

## Technologies
    - Java 11
    - Spring Boot 2.7.4
    - Maven
    - AWS DynamoDB
    - Docker
    - JUnit
    - Mockito

## Instructions
![image](https://github.com/francomiro/Fivvy-Backend-Challenge/assets/38414853/1b03bba0-084c-4fd0-b744-00ceedab611b)

## Run with Docker
To run the API using Docker, follow the steps below:

   1. Make sure you have Docker installed on your system.
   2. Clone the repository to your local computer.
   3. Navigate to the root directory of the project (folder "solution") and open the console.
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
### Postman
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/21855960-3ffda50c-a170-4229-8e3e-dd26deb82d34?action=collection%2Ffork&collection-url=entityId%3D21855960-3ffda50c-a170-4229-8e3e-dd26deb82d34%26entityType%3Dcollection%26workspaceId%3D0614edc3-8799-45d9-8005-77b2f32c3f7b)

### Endpoints of Disclaimer
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
- Request Example:

        {
            "name":"Terms and Conditions",
            "text":"Terms and Conditions of the contract",
            "version":"1.0.0"
        }

#### Update Disclaimer

- Method: PUT
- URL: `http://localhost:8080/disclaimer/`
- Request body: JSON.
- Description: This endpoint updates an existing disclaimer with the data provided in the request body.
- Request Example:

        {
            "id":"972c1826-fee1-4cbb-814f-44c9170f65eb",
            "text":"Text update",
            "name":"Name update",
            "version":"1.0.1"
        }

#### Delete Disclaimer

- Method: DELETE
- URL: `http://localhost:8080/disclaimer/{disclaimerId}`
- Request param:
    - `disclaimerId`: ID of the disclaimer to delete.
- Description: This endpoint removes the disclaimer with the specified ID.
- Request Example:

        DELETE http://localhost:8080/disclaimer/972c1826-fee1-4cbb-814f-44c9170f65eb


### Endpoints of Acceptance
#### List Acceptances

- Method: GET
- URL: `http://localhost:8080/acceptance/`
- Request Param:
  - `userId` (optional): Allows you to filter Acceptances by user ID.
- Description: This endpoint returns an Acceptance list based on the supplied parameters. Optionally, the `userId` parameter can be provided to filter Acceptances  by user ID.
- Request Examples:
        
        GET http://localhost:8080/acceptance/

        GET http://localhost:8080/acceptance/?userId=fmiro

 #### Create Acceptance

- Method: POST
- URL: `http://localhost:8080/acceptance/`
- Request body: JSON
- Description: This endpoint creates a new acceptance with the data provided in the request body.
- Request Example:

        {
            "disclaimerId":"972c1826-fee1-4cbb-814f-44c9170f65eb",
            "userId":"fmiro"
        }


