.PHONY: build run

build:
	mvn clean compile assembly:single

run:
	java -Xdebug -jar target/hello-1.0-SNAPSHOT-jar-with-dependencies.jar
