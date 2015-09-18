pkg=pilot
app=Pilot
J=java
JC=javac
src=src
bin=build

all:
	find $(src) -name "*.java" |xargs $(JC) -d $(bin) -Xlint:all
	if [ ! -f pilot.properties ]; then ln -s $(src)/pilot.properties .; fi

run:
	CLASSPATH=$(bin) $(J) $(pkg)/$(app)

launch: all run

clean:
	rm -rf pilot.properties $(bin)/*
