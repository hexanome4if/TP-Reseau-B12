javac -d classes src/stream/*/*.java src/stream/*/*/*.java src/stream/*/*/*/*.java
java -classpath classes stream.client.MainClient localhost 9000
