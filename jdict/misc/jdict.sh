#!/bin/bash


CURRENT_DIRECTORY=$PWD

cd $CURRENT_DIRECTORY

#-Dfile.encoding deve essere impostato ad UTF8 altrimenti ci sono dei problemi con il loader excel oppure con le classi che effettuano la coniugazione dei verbi
#-Djxl.nowarnings=true serve ad evitare messaggi noiosi di warning the jexcel stampa su stderr
#-Xms256m  -Xmx1024m"

java -Xms256m -Xmx1024m -Djxl.nowarnings=true -Dfile.encoding=UTF8  -jar jdict.jar


