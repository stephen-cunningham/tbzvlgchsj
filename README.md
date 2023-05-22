# Weather API

This service provides an API for managing and querying weather data such as temperature, wind speed and humidity. 
This data is collected from weather sensors, and can be queried via various API endpoints.

## Getting Started
1. Clone the repository
2. Open with the IDE of your choice
3. Navigate to the `src/main/java` directory
4. Run the `WeatherApplication` class

## The design
### Database tables
There are three distinct database tables:

| `WEATHER_STATIONS` | `WEATHER_SENSORS`            | `WEATHER_DATA_POINTS` |
|--------------------|------------------------------|-----------------------|
| ID                 | ID                           | ID                    |
| LONGITUDE          | WEATHER_STATION_ID           | WEATHER_SENSOR_ID     |
| LATITUDE           | LAST_DATA_CREATION_TIMESTAMP | METRIC_TYPE           |
|                    | SENSOR_STATUS                | METRIC_VALUE          |
|                    |                              | METRIC_TIMESTAMP      |

It was designed like this with a focus on keeping each piece of data strictly tied to a particular location.

That is, each weather data point is tied to a weather sensor. This is based on the assumption that one sensor has a specific job. e.g. to record wind speed.

Each sensor is tied to a weather station. Any one weather station is assumed to comprise of several different weather sensors.
So if a weather station is at longitude 53.2714 and latitude 9.0489, it will have several different sensors to detect changing weather conditions. 
While these sensors may technically be in slightly different locations, this is ok for the purposes of a POC. Of course, some requirements gathering would have to be conducted to 
confirm if this was an ok assumption/restriction for a production-scale application.

Sensors have one purpose and one purpose only. That is, a sensor does not report both wind speed and humidity. Again, this is an assumption made and it may be unsuitable, but more familiarity with the domain/requirements would help to inform this decision.

### Metric value representation
Each data point is represented as a `Double`. That's because all metrics mentioned in the spec, as well as any other weather metrics I discovered, can all be represented as such.
One interesting bit of work was deciding what to do about supporting different units of measure, e.g. Celsius or Fahrenheit.

My initial thought was to allow for both to represented in the database and the API. We could require sensors to report temperature in Kelvin and then implement converters to work out the Celsius and Fahrenheit values.
However, this would require additional weight in the payloads coming from the sensors, and we probably want to limit that amount of data that they need to generate/send.
Secondly, it would introduce additional complexity and work, perhaps unnecessarily. I felt it was best to go with simplicity for now. More importantly though, I feel like 
dealing with only one unit of measurement for each metric makes sense to guarantee data integrity. 

Some consideration was given to the fact that a significant proportion of the company's customer base live in the United States, where Fahrenheit is used. However, if we ever integrate this API with a front-end, the client code could perform the conversion based on what's needed.

### Laying the groundwork for future development
Built into `WEATHER_SENSORS`, we have two fields that aren't yet directly used in the application. They are `SENSOR_STATUS` and `LAST_DATA_CREATION_TIMESTAMP`. They will be useful in the future.
Sensor status represents the current state of the sensor. Its allowed values are:

| Value         | Meaning                                                                                                                                                                                                                                             |
|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `LIVE`        | This indicates that the sensor is actively collecting data without major issues                                                                                                                                                                     |
| `FAULT`       | There has been some kind of failure. This can help to identity infrastructure issues and help with availability                                                                                                                                     |
| `MAINTENANCE` | Currently under maintenance due to a prior (or anticipated) failure                                                                                                                                                                                 |
| `RETIRED`     | The sensor is no longer in use. `DELETE` endpoints aren't exposed for sensor, because we want to keep track of them in perpetuity. Instead, an API can be developed in the future that disables the sensor, while keeping its information persisted |

The last data creation timestamp (could perhaps have a better name) keeps track of the last metric that was reported by the sensor. This can help to identify issues in the infrastructure before they become serious.
For example, if sensors report data every 10 seconds and a sensor has gone 60 seconds without a report, it can be flagged for inspection. If we need high availability, this can be done by a set of workers that constantly poll our sensors for health updates.

This is achieved by using AOP - whenever a data point is saved, this update happens. We may want to revise this so that we don't have two DB writes every time a sensor sends data. Again, this highly depends on what what the needs of the system are, the infrastructure available, and the NFRs.

## The API
### How to interact
You have two options:
1. Use Swagger/OpenAPI
The application runs on port 16001. You can change this by adjusting the `server.port` property in `application-default.properties` in the `src/main/java/resources` folder.
You can access Swagger here: http://localhost:16001/swagger-ui/index.html

2. Use a client of your choice, such as Postman
I have provided a useful Postman API collection here to help with your testing:
https://api.postman.com/collections/27524377-d682cd23-7a88-453c-a331-8f579916fa8f?access_key=PMAT-01H11BNEACPPH1X86BQ90NSXC6

Alternatively, there is a JSON dump included in the base folder of this repository - `postman-dump.json`. You can use this to access the collection.

### Data
There is some default data provided for testing purposes. You can view/edit this data by opening the `data.sql` file in the `src/main/java/resources` folder.

### A note on the spec
In the coding challenge spec, there is a requirement to implement a query in such a way that:
* If a date range is specified, is must be at least 24 hours, and at most one calendar month
* If no date range is specified, we should get the latest metric reading

I felt that there were some issues with this, and thus provided a slightly alternate solution, while still providing the required functionality.

1. I implemented a general `GET` query at `/weatherDataPoints/search/query`. This allows for the specification of a date range:
* If no range is specified, the last 24 hours is used.
* If a start time is provided, but no end time, then start time -> start time + 24 hours is used
* If an end time is provided, but no start time, then end time - 24 hours -> end time is used
* If both are specified, validation is performed to ensure that the constraints are followed: 24h <= range <= One month 
2. I also implemented a `GET` query at `/weatherDataPoints/search/findMostRecentByWeatherSensorId`. This queries the most recent data point

I felt that this was a more appropriate solution, because it clearly separates the concerns of (A) querying by date range (with aggregation being the key goal) and (B) finding the latest metric reading.
I feel like this leads to a better structure in the API definition and makes things easier to understand for new API users.
It will also help to avoid any issues that may arise from making our queries more complex.

## Things that could be improved
### Inefficient Code
While the implementation provided is fully functional, there are some issues. Namely, the querying of results is quite inefficient.
This is primarily due to what seems to be an outstanding bug that came with the Spring Boot 3 upgrade.

This issue is specifically in relation to using a `something in (:listOfSomethings)` in a query, e.g.:
`select a from #{#entityName] a where a.myField in :myFieldListOfValues`

This issue has been reported and acknowledged as an issue by the Spring team:
* https://github.com/spring-projects/spring-data-jpa/issues/2829
* https://github.com/spring-projects/spring-data-jpa/issues/2904

The root cause is a Hibernate issue:
* https://hibernate.atlassian.net/browse/HHH-16236

As a result of this, the implementation provided has to make an individual call for each ID specified when querying 
(this doesn't affect calls where we don't specify and ID, i.e. retrieving all data, so while inefficient, it's not as bad as it may seem. 
The severity of the impact would of course depend on use cases).

Going forward, addressing this would be a top priority before releasing this code - it would not be prudent to deploy this code into production with these issues.
If no fix was provided for this by the Hibernate and/or Spring teams, an alternative solution would have to be sought.

### Security
Security should be implemented across the application. I didn't do so due to time constraints, since the API isn't publicly available, and because it is simply a POC.

### Improve test coverage
I have written a number of unit tests and integration tests. Test coverage is currently ~90%:
* class coverage: 100% (31/31)
* method coverage: 91% (65/71)
* line coverage: 92% (159/171)
This is decent, but I would like to improve it. In particular, I would like to put additional efforts into adding more integration tests.

Some small additional effort would easily push the test coverage into the high 90s, which would be nice and comprehensive.

### Develop a Jenkins pipeline
While potentially out of the scope of a quick POC, it would be great to have a Jenkins pipeline. This would allow for things such as test automation and vulnerability scanning whenever code is pushed/merged.  

### Deploy code into a live environment
It would be nice to deploy this code onto EC2. The hope was to have time to do this, but I only had a short period of time to code this.
I was also going to use Amazon RDS with MySQL to implement a live database.

## Metric units of measurement
It may be worth considering being more explicit about the units of measurement. This can either be done in the code, or we can assume that users will be able to refer to the documentation for details such as:

| Metric       | Unit of Measurement | Example           |
|--------------|---------------------|-------------------|
| Temperature  | Celsius             | -15.2 or 38.3 (C) |
| Humidity     | Percentage          | 30 (%)            |
| Pressure     | Hectopascals        | 1020 (hPa)        |
| Wind speed   | Metres/Second       | 15 (m/s)          |

Please see the [Metric value representation](#Metric value representation) section above for a more in-depth conversation around the representation of data in the database. 

## Additional features
* Health score algorithm
It might be nice to implement some sort of health score algorithm for the sensors. In addition to what was mentioned above under [Laying the groundwork for future development](#Laying the groundwork for future development), this could take the number of expected data points in a week and compare it with the expected number of data points.
We could also potentially weight these scores - higher disparities in the last hour could count for more than outages seven days ago.
* Health check endpoint
It might also be helpful to implement a health check endpoint to poll sensors manually, rather than having to wait for an automated request coming from the sensors.

## Final Note
This has been quite fun, so I think I'll keep working on this if I have some free time in the evenings! Feel free to pay attention to the repo for additional commits, or take this version as my submission - whatever works best for you :)

If you would like to see the Jira board I quickly threw together for tracking my work on this project, as well as some of the tasks I didn't get around to completing, you can find it here: https://stepheninterview.atlassian.net/jira/software/projects/WA/boards/1

If you can't access it, please reach out to me at stephencunningham@outlook.ie and I will provide you with a credential.