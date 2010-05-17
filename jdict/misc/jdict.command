#!/bin/bash



export DIRE=`echo $0 |sed s/jdict.command//`
cd  $DIRE

#-Dfile.encoding DEVE essere impostato ad UTF8 per la corretta visualizzino dei caratteri UTF8
#-Djxl.nowarnings=true serve ad evitare messaggi noiosi di warning the jexcel stampa su stderr
#-Xms256m  -Xmx1024m"

java -Xms256m -Xmx1024m -Djxl.nowarnings=true -Dfile.encoding=UTF8  -jar jdict.jar


