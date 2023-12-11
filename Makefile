all: compile
compile: 
	javac -cp .:lib/jbotsim-standalone-1.2.0.jar -d build src/*.java

run: compile
	java -cp .:lib/jbotsim-standalone-1.2.0.jar:build Main

clean:
	rm -rf build/
