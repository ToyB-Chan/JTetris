@ECHO OFF
START powershell.exe -Command "javac ./Main.java; ./set-console-mode.ps1; java ./Main.java | Out-Host"