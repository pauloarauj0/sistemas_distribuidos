read -p "Enter Port: " port
javac $(find ds/trabalho/parte1/ -name "*.java")
java ds.trabalho.parte1.Peer localhost $port
