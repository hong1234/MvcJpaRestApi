// Hong Le Nguyen

REST API Demo using Spring 5 (Boot) MVC, JPA, Security, Test

git clone https://github.com/hong1234/MvcJpaRestApi.git

cd MvcJpaRestApi

//test

mvn test

//run app

mvn spring-boot:run

//swagger UI
http://localhost:8000/swagger-ui.html

// client curl commande

// get -------

// curl -s http://localhost:8000/api/books | jq

// search books with title substring = "Spring"

curl -s http://localhost:8000/api/books/search?title=Spring -u user:password | jq

// get all books

curl -s http://localhost:8000/api/books -u user:password | jq

// get reviews of book Id=2

curl -s http://localhost:8000/api/books/2/reviews -u user:password | jq

// post -------

// add book without reviews

curl -i -X POST -H "Content-Type: application/json" -d '{"title":"Spring Intro", "content":"Spring in one day"}' http://localhost:8000/api/books -u admin:password

// add book with reviews

curl -i -X POST -H "Content-Type: application/json" -d '{"title":"My City", "content":"roman","reviews": [{"name": "hong","email": "hong@gmail.com", "content": "good"}]}' http://localhost:8000/api/books -u admin:password

// add review to bookId = 2

curl -i -X POST -H "Content-Type: application/json" -d '{"name":"john", "email": "john@gmail.com", "content":"Good Book"}' http://localhost:8000/api/books/2/reviews -u user:password

// update ------

// update book Id=1

curl -i -X PUT -H "Content-Type: application/json" -d '{"title":"Spring Intro Updated", "content":"book1 update"}' http://localhost:8000/api/books/1 -u admin:password


// delete --------

// delete book Id = 3

curl -X DELETE http://localhost:8000/api/books/3  -u admin:password

// delete review id =8  of book id = 1

curl -X DELETE http://localhost:8000/api/books/1/reviews/8  -u admin:password
