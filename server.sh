javac -d classes src/*/*/*.java src/*/*/*/*.java src/*/*/*/*/*.java
java -classpath classes stream.server.MainServer 9000
