# Project Description
This project is about displaying stocks and stock exchanges information on UI. 
It allows you to do the following functionalities.
- You can select a stock exchange and see the underneath stocks for the selected stock exchange
- You can create a stock in the H2 in memory database
- You can add a stock to the selected stock exchange

# How to start the project?
Clone the code from  and import it as a maven project in the IDE like Intellij or Eclipse.
cd backend/exchange and Run the Application as a Spring Boot App from the IDE or run the command from the terminal: mvn spring-boot:run
cd frontend/stock-exchange and run the below mentioned commands:
- Make sure you have npm or yarn set up in your system
- run npm install/yarn install to install all the dependencies
- Then run npm start/yarn start tos tart the application and go to http://localhost:4200/ to view the application
- Also, you can run the command: yarn test/npm test to run the unit tests.

# More info about the project
Backend REST APIs are using H2 datatbase of spring boot and there is a schema file which loads some data in the database upon initialization.
There is a login screen and sign up screen to register new users in the DB.
You can use existing added users in the DB to login. Please find below their details:
- username: john.doe@test.com, password: qwerty
- username: jane.doe@test.com, password: qwerty
There is a POSTMAN collection also given in the backend folder
