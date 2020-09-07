all: clean compile run

compile:
	@echo "Compiling..."
	@javac -classpath ".:sqlite-jdbc-3.20.0.jar" srcCode/*.java -Xlint

run:
	@echo "Starting..."
	@java -classpath ".:sqlite-jdbc-3.20.0.jar" srcCode.NewServer 

	
clean:
	@echo "Cleaning..."
	@find . -iname *.swp -delete
	@find . -iname *.swo -delete
	@find . -iname *.class -delete
	
	
# http://192.168.10.30:5060/ to access the socket/port. The address is the vagrant address.
#@java -classpath ".:sqlite-jdbc-3.20.0.jar" esxoli.NewServer