# Welcome to the back-end for SafetyNet Alerts!

This repository holds the services which power SafetyNet Alerts.

## **Tech Stack:**
- Spring Boot
- Maven for builds
- Git repository hosted on Github for Code Versioning
- Jackson for JSON parsing
- JUnit for unit testing
- JaCoCo for code coverage measuring
- Log4j for logging

## **Endpoints:**

- /person
1. Add a new person
2. Update an existing person (first and last name do not change)
3. Delete a person (using a combination of first and last name as unique identifier)

- /firestation
1. Add a fire station/address mapping
2. Update a fire station number assigned to an address
3. Delete a mapping of a fire station or an address

- /medicalRecord
1. Add a medical Record
2. Update an existing medical record (first and last name do not change)
3. Delete a medical record (using a combination of first and last namd as unique identifier)

## **URLs:**
- /firestation?stationNumber=<station_number>

- /childAlert?address=<address>

- /phoneAlert?firestation=<firestation_number>

- /fire?address=<address>

- /flood/stations?stations=<list of station_numbers>

- /personInfoLastName=<lastName>

- /communityEmail?city=<city>