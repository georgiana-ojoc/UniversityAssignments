# RESTful API in Node.js with MongoDB Atlas
## ```http://localhost:3000``` (```404```)
## Readers
```
{
	"_id": ObjectId (string),  
	"name": string,  
	"age": integer in [0, 150]  
}
```
### ```GET /readers```
 - returns all readers
#### Query parameters
 - ```id``` (string) - returns all readers with specified identifier
 - ```name``` (string) - returns all readers with specified name
 - ```age``` (integer in [0, 150]) - returns all readers with specified age
 - ```sort_by```: ```id```, ```-id```, ```name```, ```-name```, ```age```, ```-age``` - returns all readers sorted by identifier, name or age in ascending or descending (```-```) order
 - ```skip``` (non-negative integer) - omits the first ```skip``` readers
 - ```limit``` (integer) - caps the number of returned readers by ```limit```
#### Status codes
 - ```200```: all good
 - ```400```: invalid query parameters
 - ```404```: no reader found
 - ```500```: database error
### ```POST /readers```
 - creates reader
 - sends ```Location``` header with  ```http://localhost:3000/readers/{id}```
#### Status codes
 - ```201```: all good
 - ```400```: invalid body
 - ```500```: database error
### ```PUT /readers```,  ```PATCH /readers```, ```DELETE /readers``` (```405```)
### ```GET /readers/{id}```
 - returns reader with specified identifier
#### Status codes
 - ```200```: all good
 - ```400```: invalid identifier
 - ```404```: reader not found
 - ```500```: database error
### ```POST /readers/{id}```
 - creates reader with specified identifier
#### Status codes
 - ```201```: all good
 - ```400```: invalid identifier, invalid body
 - ```409```: reader already exists
 - ```500```: database error
### ```PUT /readers/{id}```
 - updates/replaces reader with specified identifier
#### Status codes
 - ```204```: all good
 - ```400```: invalid identifier, invalid body
 - ```404```: reader not found
 - ```500```: database error
### ```PATCH /readers/{id}```
 - updates/modifies reader with specified identifier
#### Status codes
 - ```204```: all good
 - ```400```: invalid identifier, invalid body
 - ```404```: reader not found
 - ```500```: database error
### ```DELETE /readers/{id}```
 - deletes reader with specified identifier
#### Status codes
 - ```204```: all good
 - ```400```: invalid identifier
 - ```404```: reader not found
 - ```500```: database error
### ```/readers/{id}``` (```405```)
## Books
```
{
	"_id": ObjectId (string),  
	"title": string,
	"author": string,  
	"year": integer in [0, 2021]  
}
```
### ```GET /books```
 - reads all books
#### Query parameters
 - ```id``` (string) - reads all books with specified identifier
 - ```title``` (string) - reads all books with specified title
 - ```author``` (string) - reads all books with specified author
 - ```year``` (integer in [0, 2021]) - reads all books with specified year
 - ```sort_by```: ```id```, ```-id```, ```name```, ```-name```, ```age```, ```-age``` - reads all books sorted by identifier, title, author or age in ascending or descending (```-```) order
 - ```skip``` (non-negative integer) - omits the first ```skip``` books
 - ```limit``` (integer) - caps the number of read books by ```limit```
#### Status codes
 - ```200```: all good
 - ```400```: invalid query parameters
 - ```404```: no book found
 - ```500```: database error
### ```POST /readers```
 - creates book
 - sends ```Location``` header with  ```http://localhost:3000/books/{id}```
#### Status codes
 - ```201```: all good
 - ```400```: invalid body
 - ```500```: database error
### ```PUT /books```,  ```PATCH /books```, ```DELETE /books``` (```405```)
### ```GET /books/{id}```
 - reads book with specified identifier
#### Status codes
 - ```200```: all good
 - ```400```: invalid identifier
 - ```404```: book not found
 - ```500```: database error
### ```POST /books/{id}```
 - creates book with specified identifier
#### Status codes
 - ```201```: all good
 - ```400```: invalid identifier, invalid body
 - ```409```: book already exists
 - ```500```: database error
### ```PUT /books/{id}```
 - updates/replaces book with specified identifier
#### Status codes
 - ```204```: all good
 - ```400```: invalid identifier, invalid body
 - ```404```: book not found
 - ```500```: database error
### ```PATCH /books/{id}```
 - updates/modifies book  with specified identifier
#### Status codes
 - ```204```: all good
 - ```400```: invalid identifier, invalid body
 - ```404```: book not found
 - ```500```: database error
### ```DELETE /books/{id}```
 - deletes book with specified identifier
#### Status codes
 - ```204```: all good
 - ```400```: invalid identifier
 - ```404```: book not found
 - ```500```: database error
### ```/books/{id}``` (```405```)
## Register
```
{
	"_id": ObjectId (string),  
	"reader_id": ObjectId (string),  
	"book_id": ObjectId (string)
}
```
### ```GET /readers/{id}/books```
 - returns books read by person with specified identifier
#### Status codes
 - ```200```: all good
 - ```404```: no book found
 - ```500```: database error
### ```POST /readers/{id}/books```
- adds book read by person with specified identifier
#### Status codes
 - ```201```: all good
 - ```400```: invalid reader identifier, invalid book identifier
 - ```404```: reader not found, book not found
 - ```409```: book already added
 - ```500```: database error
### ```DELETE /readers/{id}/books```
 - deletes book read by person with specified identifier
#### Status codes
 - ```204```: all good
 - ```400```: invalid reader identifier, invalid book identifier
 - ```404```: book not found
 - ```500```: database error
### ```GET /books/{id}/readers```
 - returns people who read book with specified identifier
#### Status codes
 - ```200```: all good
 - ```404```: no reader found
 - ```500```: database error
### ```POST /books/{id}/readers```
 - adds person who read book with specified identifier
#### Status codes
 - ```201```: all good
 - ```400```: invalid reader identifier, invalid book identifier
 - ```404```: reader not found, book not found
 - ```409```: reader already added
 - ```500```: database error
### ```DELETE /books/{id}/readers```
 - deletes person who read book with specified identifier
#### Status codes
 - ```204```: all good
 - ```400```: invalid reader identifier, invalid book identifier
 - ```404```: reader not found
 - ```500```: database error