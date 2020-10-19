javac -d classes src/*/*/*.java src/*/*/*/*.java src/*/*/*/*/*.java
java -classpath classes stream.client.MainClient localhost 9000
