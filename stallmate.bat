@echo off
REM Compile Java files
javac -cp "lib\mysql-connector-j-9.2.0.jar" -d out ^
 src\main\java\com\puplagoon\pos\App.java ^
 src\main\java\com\puplagoon\pos\controller\*.java ^
 src\main\java\com\puplagoon\pos\service\*.java ^
 src\main\java\com\puplagoon\pos\model\dao\*.java ^
 src\main\java\com\puplagoon\pos\model\dto\*.java ^
 src\main\java\com\puplagoon\pos\view\*.java ^
 src\main\java\com\puplagoon\pos\view\components\*.java ^
 -sourcepath src\main\java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b
)

echo Compilation successful.

REM Copy resources
xcopy /s /y "src\main\resources" out\resources\

if %errorlevel% neq 0 (
    echo Failed to copy resources!
    pause
    exit /b
)

echo Resources copied successfully.

REM Run the application
java -cp "out;lib\mysql-connector-j-9.2.0.jar;out\resources" src.main.java.com.puplagoon.pos.App

if %errorlevel% neq 0 (
    echo Application failed to run!
    pause
    exit /b
)

pause