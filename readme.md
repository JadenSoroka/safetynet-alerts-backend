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
this URL must return a list of people covered by the corresponding fire station. So, if
the station number = 1, it must return the residents covered by station number 1. The
list must include the following specific information: first name, last name, address,
phone number. Additionally, it must provide a count of the number of adults and the
number of children (any individual aged 18 years or younger) in the served area.


- /childAlert?address=<address>
This URL must return a list of children (any individual aged 18 years or younger) living
at this address. The list must include the first name and last name of each child, their
age, and a list of other household members. If no children are found, this URL may
return an empty string.


- /phoneAlert?firestation=<firestation_number>
This URL must return a list of phone numbers of residents served by the fire station. It
will be used to send emergency text messages to specific households.


- /fire?address=<address>
This URL must return the list of residents living at the given address as well as the fire
station number serving the address. The list must include the name, phone number,
age, and medical history (medications, dosages, and allergies) of each person. 


- /flood/stations?stations=<list of station_numbers>
This URL must return a list of all households served by the fire station. This list must
group people by address. It must also include the name, phone number, and age of
the residents and display their medical history (medications, dosages, and allergies)
alongside each name.


- /personInfoLastName=<lastName>
This URL must return the name, address, age, email address, and medical history
(medications, dosages, and allergies) of each resident. If multiple people have the
same last name, they must all appear.


- /communityEmail?city=<city>
This URL must return the email addresses of all residents in the city