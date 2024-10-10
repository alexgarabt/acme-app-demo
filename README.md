# Design of Software
Demo app simulating a fictician app for a private healthcare company, the app provides some use cases for the doctors, ambulance personal and call center attendance for people that is assurance.  

The app is designed with the use of different **Arquitecture and design patterns**.  

## 1. Structure of the app

The Structure of the app is divided in the app and the models that defines the structure of the app.
```
.
├── app
│   ├── db
│   │   ├── config.db
│   │   └── scriptSQL.sql
│   └── netbeansProject
│       └── acme
└── models
    └── extra.asta
```

    1- app/db contains data for the MySQL DB
        * config.db contains the url for access to DB, user and password
         * scriptSQL.db contains the schema for the complex DB and simple of population of it for the USE CASES
    2- models/extra.asta contains and Astha file with all the design of the Proyect in UML schemas (packages, patterns, archicteture, use, diagram class, process diagram & more type of schemas)
    3- neatbeansProject/acme contains the Maven project with all the source code

## Architecture & Desing patterns
The app is divided into (interface) (buisness rules/ use cases) (persistence). inside path app/netbeansProject/acme/

    1-  Interface contains all the source code for the GUI (using pattern MVC) inside each package for a use case
        * One manager (interface/) of all the views (Singleton)
        * Model is in the negocio/ package (buisness) and determines the use case execution
        * Control is the Ctrl**.java classes that interacts with the model
        * View is the Vista**.java that contains the GUI and interacts with the Ctrl**.java based on events
    2- Buissnes model (negocio/) is divided into
        * Use case controler => **cotroladorescasouso/** have the **.java rules for each use case of the app.
        * Models of the domain => **modelos/** contains the models for each object that stores info, each class has a contructor that acceps a json string to build the object as DTO(Data Transfer Object) pattern to transform the data that comes from or to the DB.

    3- Persitence (persistencia/) has all the source code to interact with the database
        * **daos/** contains all the DAO object to access to the database to retrive or upload the info & objects in the database. Each DAO converts the DB data into JSON string type to send to the obj constructor to get the java obj
        * **DBAccess/** Singleton pattern to have only on point access to the DB

## Run the app
    1- First set up the DB MySQL to the config in the `config.db` file
    2- Run the SQL script `scriptSQL.sql` to set up the schema and basic info in the DB
    3- inside app/netbeansProject/acme $mvn compile
    4- Run the Main java class inside interface
