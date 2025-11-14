
1. This app uses JPA with an in memory DB for persistence, fronted by REST API and protected by JWT auth. The javascript calls this API to render in the UI.
2. The application can be accessed at /login.html 
3. username and password is my-school/awesome
example of auth and API call: (I am using windows, please remove backslashes and replace the outer " with ' in -d):
curl POST http://localhost:8080/school/auth/login -H "Content-Type: application/json" -d "{\"username\":\"my-school\",\"password\":\"awesome\"}"
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJteS1zY2hvb2wiLCJpYXQiOjE3NjMwOTA5NzYsImV4cCI6MTc2MzA5NDU3Nn0.qMHFSyNUvwSRRhQ4fYwqy4FHxxo9Gx9gF70MSZE7GzY" http://localhost:8080/school/students
4. Rate limiting 10 API calls per min and uses the token bucket algorithm. So invoking the 2nd command above 10 times will start giving 429 errors.
5. The code is in https://github.com/rshank112/playground/
6. There are some screenshots of AWS deployment in the screenshots directory and the running app. The running app screenshot is called App_Running_in_AWS.png
7. I had to delete all the AWS resources because of the cost. I can send you the yaml file of all the resources I used to deploy the docker image and get it running on AWS. I cannot push it to github rejects anything which has secrets.
8. There is a lot to be done for making this production ready off course. To start with it needs to be secured with SSL and so on. This is a very basic web app satisfying what was asked.
9. The dockerfile is at the root directory and builds an image around 1.1GB. Can be reduced by copying the exact pieces needed by the JRE
