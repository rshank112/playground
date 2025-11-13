1. I have used spring boot to build a simple web app with JPA and in an in-memory DB, REST API for the CRUD calls which in turn uses JPA.
2. The javscript/html exposes a simple UI which calls the REST API for CRUD operations.
3. There is rate limiting for GET and Create calls
4. The built in tomcat from spring boot runs on 8080 by default. We can pass this as environment variable when running docker to run on a different port.
