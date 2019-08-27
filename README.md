## Plan Generator

### Project Overview
The Plan Generator is application built with help of Spring Boot library. It accepts specified requests 
and calculates repayment plans throughout the lifetime of a loan.

#### Deployment
By default application is starting on server.port=8080 but this parameter can be
changed on any needed(check if needed has not already busy with another process).
Port setting can be changed in application.properties file. 

Application has slf4j logger with logback.xml settings. Logger setting are set to INFO and can be change on 
another level in logback.xml file.
 
Application accepts next payload:
{ "loanAmount": "5000", "nominalRate": "5.0", "duration": 24, "startDate": "2018-01-01T00:00:01Z" } with 
headers: 
* Content-Type: application/json
* Accept: application/json

Application accepts POST requests only.

In order to start server next command should be executed:
* in project location run,  start terminal and run -> *mvn spring-boot:run* it will start server with application.
* wait while *Started Main in ... * will appear in logs.
* endpoint will be available by address: http://localhost:8080/generate-plan. POST request can be send there.

After application started we can send request to endpoint via terminal:

Example is:

Try not to use this command on wuindows. If you have git bash,  better to run it there or use postman, or built in postman in intellij idea.

curl -X POST http://localhost:8080/generate-plan -H 'Accept: application/json' -H 'Content-Type: application/json' -d '{"loanAmount": "5000", "nominalRate": "5.0", "duration": 24, "startDate": "2018-01-01T00:00:01Z"}'

If you want to check with postman request should look like next:

POST http://localhost:8080/generate-plan
Content-Type: application/json
Accept: application/json
{ "loanAmount": "5000", "nominalRate": "5.0", "duration": 24, "startDate": "2018-01-01T00:00:01Z" }


In order to terminate server just press ctrl+c and it will stop server.