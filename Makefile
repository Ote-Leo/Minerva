
JC = javac		#variable holding javac (java compiler)
CLASSPATH = ./classes/  #variable holding a path to your classes
SRCPATH = ./src/

test:  
	find ./src -name '*.java' ! -name "*Test*" | xargs $(JC) -d $(CLASSPATH)

# here test is being called when ever all is called
all: test 

clean:	 
	   rm -Rf $(CLASSPATH)    
