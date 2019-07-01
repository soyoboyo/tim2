# tim (Translation & Internationalization Manager)

## Table of contents
1. Project overview
2. Functionality
3. Technical details
4. Technologies
5. Installation guide
6. Contributing
7. Licensing

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

# 3. Technical details


# 4. Technologies
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

# 5. Installation guide (users)
0. Install docker
1. Pull backend and frontend project from dockerhub:
    * docker pull studentproject/ocado-tim-backend
    * docker pull studentproject/ocado-tim-frontend
    * docker pull studentproject/simple-web-app
2. Run postgresql database
    * docker run -it -p 5432:5432 -d -v <path to database directory>:/var/lib/postgresql/data postgres
        * where path to database directory is path to place where all database data are kept e.g.
            * docker run -it -p 5432:5432 -d -v /var/database:/var/lib/postgresql/data postgres 
3. Run apps:
    * docker run -it -p 4200:4200 -d studentproject/ocado-tim-frontend
    * docker run -it -p 8081:8080 -d -v <path to users config\>:/home/principals.json
        * where <path to users config\> is path to file with principals.json e.g.
          * docker run -it -p 8081:8080 -d -v /home/oem/Github/tim/principals.json:/home/principals.json studentproject/ocado-tim-backend
    * docker run -it -p 8080:8080 -d studentproject/simple-web-app  
4. The app is availaible on localhost or address 192.168.99.100 (depends on os)


# 6. Installation guide (developers)
Backend:  
0. Install prerequisites (OpenJDK 11, maven)
1. Run `mvn clean install`  
2. It `target` directory run `java -jar tim-0.0.1-SNAPSHOT.jar --usersConfig=<path to config file*>`
    * config file should be in json type like:  
    ![Alt text](properties.png?raw=true "Title")  
3. In main directory there is also shell script (runSpringApp) that can be use to run backend app with parameters from principals.json file (the same directory). To run, please go to `tim` directory and type in command line:  
    * `chmod u+x runSpringApp`  
    * `./runSpringApp`  
    
Frontend:  
0. Install prerequisites according to this guide https://angular.io/guide/quickstart (Node.js, node package manager, Angular)
1. Go to `/webapp` folder.  
2. Run `npm i` to install dependencies.  
3. Run `ng serve` to start the application. App will be available at `localhost:4200`.  
4. 


# 7. Docker:
0. Login
 * docker login
    * Username: studentproject
    * Password: ask Wojtek :)
1. Backend (tim directory)
    * mvn clean package
    * docker build -t studentproject/ocado-tim-backend:latest ./
    * docker push studentproject/ocado-tim-backend:latest
2. Frontend (tim/webapp directory)
    * npm run build
    * docker build -t studentprojecct/ocado-tim-frontend:latest ./
    * docker push studentproject/ocado-tim-frontend:latest
3. Presentation app (tim/example_app directory)
    * mvn clean package
    * docker build -t studentproject/simple-web-app:latest ./
    * docker push studentproject/simple-web-app:latest
 

# 8. Contributing
* Mikołaj Banaszkiewicz
* Alex Kostiukov 
* Matusz Mytnik
* Szymon Sakowicz
* Wojciech Spoton 
* Jacek Zalewski 

# 9. Licensing
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
