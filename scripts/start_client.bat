@echo off
chcp 65001 > nul
echo Starting Othello Client...

set JAVA_HOME=C:\Program Files\Amazon Corretto\jdk1.8.0_472
set PATH=%JAVA_HOME%\bin;%PATH%
for %%i in ("%~dp0..") do set REPO_DIR=%%~fi
set SRC_DIR=%REPO_DIR%\src
set OUT_DIR=%REPO_DIR%\out\production\Othello-netprog

echo Compiling...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"
dir /s /b "%SRC_DIR%\model\*.java" "%SRC_DIR%\common\*.java" "%SRC_DIR%\client\*.java" > "%REPO_DIR%\sources.txt"
"%JAVA_HOME%\bin\javac" -encoding UTF-8 -d "%OUT_DIR%" @"%REPO_DIR%\sources.txt"
del "%REPO_DIR%\sources.txt"

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

REM 画像リソースをコピー
xcopy /s /y /i "%SRC_DIR%\client\assets" "%OUT_DIR%\client\assets" > nul

echo Starting client...
"%JAVA_HOME%\bin\java" -cp "%OUT_DIR%" client.Main

pause
