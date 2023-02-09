@ECHO OFF
START powershell.exe -Command "javac ./Main.java; Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy Unrestricted -Force; ./set-console-mode.ps1; java -cp . Main | Out-Host"