# README #

### What is this repository for? ###

* Basic application for sending requests with text to be translated. The interface is by means of a REST API that communicates with a database (MongoDB).
The API can return either XML or JSON answers. It allows some basic functionality:
	- Add users
	- Retrieve users (e.g. ./rest/users/001 retrieves user with UID == 001)
	- Retrieve ALL users (by means of clicking on a particular button)
	- Add request (a 'Text' object that has an UID)
	- Retrieve texts (e.g. ./rest/texts/001 retrieves text with UID == 001)
	- Retrieve ALL texts (by means of clicking on a particular button)	

* Version 0.3
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
	Import the project to Eclipse. Enjoy it with the 'Run' -> 'Run on server' option.
* Configuration
	MongoDB, Tomcat, Eclipse; Maven works out the dependencies.
* Dependencies
	They are already set up by configuration of Maven.
* Database configuration
	Use the file config to change the parameters (e.g. port or name of database); when you start the server, this information is automatically loaded.
* How to run tests
	You can use the package 'test' for playing around with database and other functionality.
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Stijn or Jose