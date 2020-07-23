# tim (Translation & Internationalization Manager)

## Table of contents
1. Project overview
1. Functionality
1. Technical details
1. Technologies
1. Installation guide
1. Contributing

# 1. Project overview
Tool enhancing cooperation between teams of developers and translation agencies.

Main features:
* allowing programmers to add to the system messages that require translations
* good integration with other systems used in software development, that is:
  * exporting reports with missing translations
  * exporting ready translations in format acceptable by Spring applications

# 2. Functionality
For all users:
* exporting reports with missing translations

For developers:
* adding, removing, editing projects
* adding, removing, editing messages to translate
* exporting properties files with ready translations

For translators:
* adding, editing, removing, invalidating translations of messages


# 3. Web pages
* Frontend
    * https://studentproject-tim-frontend.herokuapp.com
* Backend
    * https://studentproject-tim-backend.herokuapp.com


# 4. Technical details
Accounts available in project:
* tran / tran / ROLE_TRANSLATOR
* prog / prog / ROLE_DEVELOPER
* adminTran / adminTran / ROLE_ADMIN_TRANSLATOR
* adminProg / adminProg / ROLE_ADMIN_DEVELOPER
* admin / admin / ROLE_SUPER_ADMIN


# 5. Technologies
* Backend
  * [Java 11](https://openjdk.java.net/projects/jdk/11/)
  * [Spring 5](https://spring.io/)
  * [JUnit 5](https://junit.org/junit5/)
  * [Mockito 2](https://site.mockito.org/)
* Frontend
  * [Angular 7](https://angular.io/)
  * [Angular Material 7](https://material.angular.io/)
* Other
  * [Swagger UI](https://swagger.io/)

# 6. Installation guide (users)
1. Install docker
1. Pull backend and frontend project from dockerhub:
    * docker pull studentproject/ocado-tim-backend
    * docker pull studentproject/ocado-tim-frontend
    * docker pull studentproject/simple-web-app
1. Run postgresql database
    * docker run -it -p 5432:5432 -d -v <path to database directory>:/var/lib/postgresql/data postgres
        * where path to database directory is path to place where all database data are kept e.g.
            * docker run -it -p 5432:5432 -d -v /var/database:/var/lib/postgresql/data postgres 
1. Run apps:
    * docker run -it -p 4200:4200 -d studentproject/ocado-tim-frontend
    * docker run -it -p 8081:8080 -d studentproject/ocado-tim-backend
    * docker run -it -p 8080:8080 -d studentproject/simple-web-app  
1. The app is available on localhost or address 192.168.99.100 (depends on os)


# 7. Installation guide (developers)
Backend:  
1. Install prerequisites (OpenJDK 11, maven)
1. Run `mvn clean install`  for `backend` and `aut_server` applications

    
Frontend:  
0. Install prerequisites according to this guide https://angular.io/guide/quickstart (Node.js, node package manager, Angular)
1. Go to `/frontend` folder.  
1. Run `npm i` to install dependencies.  
1. Run `ng serve` to start the application. App will be available at `localhost:4200`.  


# 8. Docker:
0. Login
 * docker login
    * Username: studentproject
    * Password: ask Wojtek :)
1. Backend (tim directory)
    * mvn clean package
    * docker build -t studentproject/ocado-tim-backend:latest ./
    * docker push studentproject/ocado-tim-backend:latest
2. Frontend (tim/frontend directory)
    * npm run build
    * docker build -t studentprojecct/ocado-tim-frontend:latest ./
    * docker push studentproject/ocado-tim-frontend:latest
3. Presentation app (tim/example_app directory)
    * mvn clean package
    * docker build -t studentproject/simple-web-app:latest ./
    * docker push studentproject/simple-web-app:latest
 

# 9. Contributing
* Mikołaj Banaszkiewicz
* Alex Kostiukov 
* Matusz Mytnik
* Szymon Sakowicz
* Wojciech Spoton 
* Jacek Zalewski 
