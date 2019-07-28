# Orbit Calculation Service

Practical Spring Boot exercise of a service for the calculation of orbits of planets in Solar Systems. Rotation transformation in a two-dimensional space and calculation of climate according to positions in the plane


### Setting up
Configure `application.properties` the database connection and you are ready to go

Run with: ``$ mvn spring-boot:run``


### Swagger & requests
Go to ``http://localhost:PORT/`` (root URL) so you can visualize Swagger documentation

The Dates format must be `yyyy-mm-dd`

The last 2 GET requests of `solar-system-controller` are for pronostics consultation. After the Job is executed there will be weather information of the Planets Status to retrieve

There are entities attributes that you cannot PATCH, like SolarSystem positions and Planets sun distance because it carries to modify lot of data related to the PlanetStatus positions & weather. For this version is not programmed this patching, taking care about the performance issues that is still in design

### Heroku
Use branch ``heroku-master`` to push to Heroku cause it has in memory database configured in `application.properties` with HSQLDB so it wont crash trying to find a database.

Heroku shutdown its workers in a few time so when you enter it wont have any information but it Seeders: a SolarSystem with its 3 Planets associated

You must execute the Job that runs for ten years in the future to calculate Weather. This is an asynchronous request; it will return the Job ID with a CREATED status, and its future status can be consulted with that ID. The Job also executes itself every day by 12pm using `@Scheduled` annotation

Remember to push to `master` branch so it can be deployed
```
$ git push HEROKUORIGIN heroku-master:master
```

Here you have my App up & running ``https://apal--orbit.herokuapp.com``


### Math and Weather
There are 4 use cases for different weathers:
1. When Planets are aligned with sun (DROUGHT)
2. When Planets are aligned themselves without the sun (OPTIMAL)
3. When Planets form a triangle with sun inside (RAINFALL)
4. When Planets form a triangle with the sun outside (UNKNOWN)

For the `1` and `2` use case traces a Triangle with all three Planets. Use the greater vector magnitude as the base of the Triangle, and the Planet of the center define the height. In case the height is smaller than the radius of all the Planets then there is a line traspassing all of them. 
This approach is better than the solution of `v1.0.0` cause the thereshold that define the alignment is defined by the radius of the Planets; so there is no need to be a PERFECT alignment between them when they are close to be aligned. While a line is passing throught them, we can consider that they are already aligned

For the `3` use case it detects the collision between the sun and the triangle slicing the triangle in 3 pieces using the sun position. If the sun is inside all the triangles will have the same direction, and the intensity of the weather will be equal to the triangle perimeter; otherwise the sun is outside

For the `4` use case uses the same collision detection


### About collision detection
Why I didn't use collision detection for finding collinearity?

In game development is very popular to use collision detection by shooting a point and detect collision; but it carries a lot of processing and has two gaps of failure:

1. The first gap is the angle of the shot: if the target is too close the collision might be guaranteed; but if the target is very far away it has a gap of failure because is not the same 1º at 3 meters, than 1º at 1000km. The target could be ignored and do not detect collision

2. The second gap is the velocity of the bullet: if the target has a 10units of radius but the velocity of the bullet is 15units per iteration, target wont be detected either

Also this approach is very expensive because the iteration of the bullet



Porque yo tengo las manias que tiene el perrito cacero; que de donde me regañan me voy y donde me acarician me quedo