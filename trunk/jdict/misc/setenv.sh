

set -o vi

JE_HOME=/Applications/java/je-3.0.12/lib
export JE_HOME

LOG4J_HOME=/Users/ChristianVerdelli/Documents/workspace/JDICT/lib/
JL_HOME=/Users/ChristianVerdelli/Documents/workspace/JDICT/lib/

SITEMINDER_SDK_HOME=/Applications/java/siteminder-sdk

JAVALAYER=$JL_HOME/jl1.0.jar
MPEGSPI=$JL_HOME/mp3spi1.9.1.jar
VORBISSPI=$JL_HOME/jorbis-0.0.12.jar:$JL_HOME/jogg-0.0.5.jar:$JL_HOME/tritonus_share.jar:$JL_HOME/vorbisspi1.0.jar
JSPEEXSPI=$JL_HOME/jspeex0.9.3.jar
LOG=$JL_HOME/commons-logging-api.jar
COLLECTIONS=$JL_HOME/commons-collections-3.2.jar
CONFIGURATION=$JL_HOME/commons-configuration-1.5.jar
LOG4J=$LOG4J_HOME/log4j-1.2.14.jar
JDB=$JE_HOME/je.jar
JXL=$JL_HOME/jxl.jar
SITEMINDER_SDK=$SITEMINDER_SDK_HOME/jsafe.jar:$SITEMINDER_SDK_HOME/jsafeJCE.jar:$SITEMINDER_SDK_HOME/smagentapi.jar:$SITEMINDER_SDK_HOME/smjavaagentapi.jar:$SITEMINDER_SDK_HOME/SmJavaApi.jar:$SITEMINDER_SDK_HOME/smjavasdk2.jar

#JFormDesigner Jars
JFORM_HOME=/Applications/java/JFormDesigner/redist/
GOODIES=$JFORM_HOME/jgoodies-uif-lite.jar
FORMS=$JFORM_HOME/forms-1.0.7.jar

CLASSPATH=$JAVALAYER:$LOG:$JLGUI:$MPEGSPI:$JSPEEXSPI:$BASICPLAYER:$LOGIMPL:$VORBISSPI:$JDB:$JXL:$LOG4J:$GOODIES:$FORMS:$SITEMINDER_SDK:$COLLECTIONS

CLASSPATH=$CLASSPATH:.
export CLASSPATH


ANT_HOME=/Application/java/ant
PATH=${PATH}:${ANT_HOME}/bin
export ANT_HOME


PATH=$PATH:/usr/local/mysql/bin
export PATH



export ORACLE_HOME="/Applications/OraDb10g_home1/"
export PATH=$PATH:~/bin:$ORACLE_HOME/bin
