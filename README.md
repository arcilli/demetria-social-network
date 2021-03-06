# Demetria social network

Demetria is a social network based on a microservice architecture.

# Current status (on major functionalities)

- [x] CRUD operations (Create account, Read(display) posts, Update user information, Delete a post)
- [x] Separate user service & post service
- [ ] Register OpenGraph service with eureka
- [ ] Integrate OpenGraph with post service
- [x] Create friendship relation
- [x] Implement votes and comments on posts
- [ ] Implement & design search service - on users (and/or content)
- [ ] Implement & design Hadoop cluster/service - opinion analysis

# General architecture

![servicesDiagram](models/servicesDiagram.svg)

### Tech stack

Some of the (open source) technologies that has been used:

* [Docker] - deployment
* [Spring Boot] - IoC/general framework
* [Spring Data] - data access
* [Thymleaf] - server-side Java template engine
* [Netlix Eureka] - service registration & service discovery
* [Bootstrap] - responsive design
* [jQuery] - JS library, used especially for ajax(async) requests
* [REST] - communicate over microservices

### Installation
Demetria requires a minimum setup for Docker Compose.
Once Docker-Compose is installed:
```sh
$ docker-compose up .
```
**TODO: setup docker-compose file**
### Docker
**to be populated**
### Todos
----
Mainly, too many to enumerate. Some of them are in issues section, others (with more general character) here:
 - Create Dockerfile for microservices
 - Setup docker-compose file
 - Test (from time to time) Docker deployment
 - Write (MORE) Tests

### Screenshots
----
**Homepage** - for not logged-in/non-registered users
<p align="center">
<img src="https://i.imgur.com/o3bqyTil.png" alt="Homepage version 1"
style="width: 35%";/>
</p>

**Timeline** - homepage for logged-in users. It will contain posts from friends.
<p align="center">
<img src="https://i.imgur.com/r4ZL6jJ.png" alt="Timeline"
style="width: 50%;"/>
</p>

**User posts** - timeline restricted to user posts
<p align="center">
<img src = "https://i.imgur.com/w2QNj0ql.png" alt="Timeline"
style="padding-bottom:10px;"/>
</p>

**User settings** - contains settings for an account
<p align="center">
<img src="https://imgur.com/iHOCsDel.png" alt="Timeline"
style="width: 50%; padding-bottom:10px;"/>
</p>

#### License
----
MIT

**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[Docker]: https://www.docker.com/
[Spring Boot]: https://spring.io/projects/spring-boot
[Spring Data]: https://spring.io/projects/spring-data
[Thymleaf]: https://www.thymeleaf.org/
[Netlix Eureka]: https://github.com/Netflix/eureka
[Bootstrap]: https://getbootstrap.com/
[REST]: https://restfulapi.net/
[jQuery]: <http://jquery.com>
