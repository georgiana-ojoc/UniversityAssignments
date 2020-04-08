# Document Management System (Maven Project)
Implemented a shell to manage a serialized catalog of documents (used Singleton for shell and Command for commands as design patterns)  
The shell includes the following commands:  
 - help - show commands  
 - create <name> <path> - create catalog with specified name and path  
 - add-doc <id> <name> <location> - add document with unique ID and name from specified location  
 - add-tag <id> <key> <value> - add tag to document specified by ID  
 - save - save catalog at his specified path  
 - load <path> - load catalog from specified path  
 - list - print catalog and documents information  
 - view <id> - open document specified by ID  
 - info <path> - print file metadata (used Apache Tika)  
 - report [html] <path> - create and save html report at specified path (used FreeMarker)  
 - exit - close shell  
Use the following command to open the shell after creating the JAR:  
```
java -jar DMS-1.0-jar-with-dependencies.jar
```