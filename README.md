# fixtures

# General description  

Simple command line run application that, having as input the participating teams of a sports league, prints the season game plan.  
The logic applied is the one of the round-robin tournament where each contestant meets all other contestants in turn.  
Scheduling algorithm is described [here](https://en.wikipedia.org/wiki/Round-robin_tournament#Circle_method)

# Prerequisites  

## development mode / build process  

This is a Java based Spring Boot application. The followings are required for application development or building  
- JDK (min 1.8)   
- STS / Eclipse or any other IDE supporting Java/Spring development  
- Maven (3.x.x) 

## 'productive' mode  
- JRE (min) 1.8  


# Building and running application  

## Building the application  

Via command line, change directory to where *fixtures* project resides on the file system and run either *mvn package* or *mvn install*.  
The first option will output the executable jar - *fixtures-x.y.z.jar* - under *target* directory (*x.y.z are placeholders for current version*).  
The second option will additionally install the jar file to the local user Maven repository at {maven.repo.local}/de/tpuica/fixtures.  

## Running the application  


Via command line, assumming executable is *fixtures-1.0.jar*, change directory to where executable is on the file system, then:  
- the simplest way to run the application
	- **java -jar fixtures-1.0.jar**  
	- this will use application defaults (such as import file, season start date, etc, see list of all available properties below)  
- provide external input file for league/teams
	- **java -jar -Dapplication.resources.league=file:./soccer_teams.json fixtures-1.0.jar**  
	- above command requires file *soccer_teams.json* being present in the current directory  
	- absolute paths are supported e.g.  
		- Windows: *-Dapplication.resources.league=file:C:/data/soccer_teams.json*  
		- Unix based systems: *-Dapplication.resources.league=file:/user/data/soccer_teams.json*  
	- sample test files can be found under *src/test/resources/import*  
- change the schedule mode  
	- **java -jar -Dapplication.regularScheduleMode=true fixtures-1.0.jar**  
	- the property defaults to *false* (non regular schedule mode: one match per Saturday **only**)  
	- setting the property to *true* will schedule all matches of a round the same Saturday  
	
List of all application properties and their default values  
- *application.resources.league = classpath:import/soccer_teams.json*  
- *application.dateFormat = dd.MM.yyyy*  
- *application.seasonStartDate = 03.10.2019*  
	- must observe defined *dateFormat*
- *application.seasonBreak = 3*
	- must be a number, represents break duration in weeks  
- *application.regularScheduleMode=false*  


# NOTE  
- application logging is normally disabled, the console is only printing the game schedule
- logging can be activated by setting the dev profile active
	- **java -jar -Dspring.profiles.active=dev fixtures-1.0.jar**  

