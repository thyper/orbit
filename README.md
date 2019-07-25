# Orbit Calculation Service

Practical Spring Boot exercise of a service for the calculation of orbits of planets in Solar Systems. Rotation transformation in a two-dimensional space and calculation of climate according to positions in the plane


### Setting up
Configure `application.properties` the database connection and you are ready to go

Run with: ``$ mvn spring-boot:run``


### Swagger & requests
Go to ``http://localhost:PORT/`` (root URL) so you can visualize Swagger documentation

The Dates format must be `yyyy-mm-dd`

The last 2 GET requests of `solar-system-controller` are for pronostics consultation. After the Job is executed there will be weather information of the Planets Status to retrieve

### Heroku
Use branch ``heroku-master`` to push to Heroku cause it has in memory database configured in `application.properties` with HSQLDB so it wont crash trying to find a database.

Heroku shutdown its workers in a few time so when you enter it wont have any information but it Seeders: a SolarSystem with its 3 Planets associated

You must execute the Job that runs for ten years in the future to calculate Weather; the Job also executes itself every day by 12pm. This is an Asynchronous request; it will return the Job ID with a CREATED status, and its future status can be consulted with that ID

Remember to push to `master` branch so it can be deployed
```
$ git push HEROKUORIGIN heroku-master:master

```

Here you have my App up & running ``https://apal--orbit.herokuapp.com``
