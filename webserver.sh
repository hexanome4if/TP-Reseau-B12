javac -d classes src/httpserver/*.java src/httpserver/*/*.java
java -classpath classes httpserver.WebServer 3000
