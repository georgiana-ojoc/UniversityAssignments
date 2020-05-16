# Gomoku
Implemented a client-server application that allows playing Gomoku between a user and an AI player or between two users.  
The shell includes the following commands:  
 - list-commands - uses RMI  
 - list-available-rooms - displays the rooms that are not full  
 - create-room-single - creates a room with a user and an AI player  
 - create-room-double - creates a room with two users  
 - join-room <identifier> - identifier is a 5-character string generated when creating a room  
 - add-piece <row> <position> - adds a piece at the specified position if available  
 - stop-round - end round in order to start another one or to exit game  
 - exit - close connection  
Once a game is finished, a HTML containing the game actions is uploaded on fenrir using JCraft.  
The AI player tries to complete his 5-piece row or column or to block opponent's ones.  
Use the following command to open the server after creating the JAR:  
```
java -jar Server-1.0.jar
```
Use the following command to open a client after creating the JAR:  
```
java -jar Client-1.0-jar-with-dependencies.jar
```
