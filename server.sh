javac -d classes src/stream/*/*.java src/stream/*/*/*.java src/stream/*/*/*/*.java
java -classpath classes stream.server.MainServer 9000
