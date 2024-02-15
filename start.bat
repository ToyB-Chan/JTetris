@ECHO OFF
MKDIR out
START powershell.exe -NoExit -Command "javac -d ./out/ ./src/*.java; Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy Unrestricted -Force; ./set-console-mode.ps1; java -cp ./out/ Main | Out-Host"