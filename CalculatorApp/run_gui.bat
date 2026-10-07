@echo off
setlocal
cd /d "%~dp0"

:: Prefer JDK 21 java/javaw if present, else fallback to PATH
set "JAVA_BIN="
if exist "C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot\bin\javaw.exe" (
    set "JAVA_BIN=C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot\bin\"
)

:: Recompile with release 8 target for 100% compatibility across Java versions
"%JAVA_BIN%javac.exe" --release 8 -Xlint:-options Calculator.java 2>nul || javac --release 8 -Xlint:-options Calculator.java 2>nul || javac Calculator.java 2>nul

:: Launch the application
if not "%JAVA_BIN%"=="" (
    start "" "%JAVA_BIN%javaw.exe" Calculator
) else (
    start "" javaw Calculator
)

endlocal
