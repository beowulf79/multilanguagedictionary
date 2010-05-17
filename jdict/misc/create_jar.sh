VERSION=05
JDIT_HOME="/Users/ChristianVerdelli/Documents/workspace/Dizionario MultiLingua/"

cd "$JDIT_HOME/bin"

jar cfm ../dit_"$VERSION".jar ../jars/MANIFEST.MF *.class

cd "/Users/ChristianVerdelli/Documents/workspace/Dizionario MultiLingua/"

echo "if u want to launch the jar copy it here from the jars directory"
