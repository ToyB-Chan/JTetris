#! /bin/bash
javac -d ./out/ ./src/*.javac
stty -icanon -echo
java -cp ./out/ Main
stty icanon echo