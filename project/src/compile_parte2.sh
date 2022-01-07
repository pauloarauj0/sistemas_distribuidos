read -p "Enter Port: " port
javac $(find ds/trabalho/parte2/ -name "*.java")
java ds.trabalho.parte2.Peer localhost $port
