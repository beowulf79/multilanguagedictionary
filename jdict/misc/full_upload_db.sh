
FILE=$1

HOME=/Users/ChristianVerdelli/documents/workspace/
APPL=$HOME/JDICT/
JAVA=/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Commands/java
JAVA_OPTS="$JAVA_OPTS -Xms256m -Xmx1024m" 

if [ -a $FILE ] && [ -n "$FILE" ]
then 
	echo "Importing using input file $FILE"
	rm $APPL/data/default/*
	cd $APPL/bin
	

	Echo "Section Database Import"
	$JAVA driver dizionario loadSection $FILE

	echo "Category Database Import"
	$JAVA driver dizionario loadCategory $FILE

	echo "Importing Data"
        $JAVA $JAVA_OPTS -Dfile.encoding=UTF8 net/verza/jdict/sleepycat/datastore/DatabaseLoaderTestClass $FILE

	Echo "Creating User"
#	$JAVA  $JAVA_OPTS driver dizionario loadUser

else
	echo "input file not found"
	exit -1

fi
