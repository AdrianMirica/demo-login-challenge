# demo-login-challenge
A demo project where I implemented a simple login API with Java and Spring


To start locally a instance of MySQL you need to run the following command

docker run -d -e MYSQL_ROOT_PASSWORD=<password> -e MYSQL_DATABASE=<table-name> --name mysqldb -p 3307:3306 mysql:8.0

This values will need to match the application.properties config for database