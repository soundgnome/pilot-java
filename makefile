pkg=pilot
app=Pilot
J=java
JC=javac
src=src
bin=build

all: src
	find src -name "*.java" |xargs $(JC) -d $(bin) -Xlint:all

run:
	CLASSPATH=$(bin) $(J) $(pkg)/$(app)

launch: all run
