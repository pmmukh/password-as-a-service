# password-as-a-service

This is the project for implementing Password as a Service. It provides API's to access the data in /etc/passwd and etc/group files. 
 I chose to use a maven project in Java for this challenge, initializing the project with Spring Boot.
 The API's are designed to fetch 
1. All the users.
2. A filtered subset of users.
3. A specific user based on ID.
4. A set of groups which contain a specific user as a member.
5. All the groups.
6. A filtered subset of groups.
7. A specific group based on ID.

In case the /etc/passwd or /etc/group files are not present or contain malformed data, like a non-numeric string in the position of ID, the APIs will return a 500 Internal Server Error along with a specific error message.
 
The file paths can be configured in application.properties, under the names passwordFile and groupFile. The application.properties file also references 2 testing files, so that the unit tests can run independently.

Run instructions:
1. Run "mvn clean install" from root folder.
2. Run "mvn spring-boot:run" from root folder. 
All the API's will now be running on localhost:8080 , and can be queried appropriately.
