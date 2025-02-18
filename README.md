# demo-login-challenge
A demo project where I implemented a simple login API with Java and Spring


To start locally a instance of MySQL you need to run the following command

docker run -d -e MYSQL_ROOT_PASSWORD=<password> -e MYSQL_DATABASE=<table-name> --name mysqldb -p 3307:3306 mysql:8.0

This values will need to match the application.properties config for database

For instance, this project to work out of the book we need to run the following docker command

docker run -d -e MYSQL_ROOT_PASSWORD=coremarker -e MYSQL_DATABASE=demologin --name mysqldb -p 3307:3306 mysql:8.0

In resources, there is a postman collection to test the APIs.

/auth/register

/auth/login

There is no authorization for them, and for the other 2, /users/{username} and users/me 
you need to add the JWT Token value in the Authorization tab(select from dropdown Bearer and insert token value)