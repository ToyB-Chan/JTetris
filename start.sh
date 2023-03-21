#! /bin/bash
javac -d ./out/ ./src/*.java
stty -icanon -echo
java -cp ./out/ Main
stty icanon echo
