@echo off
chcp 65001 > nul
echo Starting Othello Client...

rem パスをクォート付きで保持
set "CORRETTO_HOME=C:\Program Files\Amazon Corretto\jdk1.8.0_472"

rem Corretto の存在チェック
if exist "%CORRETTO_HOME%\bin\javac.exe" (
    echo Using bundled JDK: %CORRETTO_HOME%
    set "JAVA_HOME=%CORRETTO_HOME%"
    set "PATH=%JAVA_HOME%\bin;%PATH%"
    set "JAVAC_CMD=%JAVA_HOME%\bin\javac"
    set "JAVA_CMD=%JAVA_HOME%\bin\java"
) else (
    echo Bundled JDK not found. Using system default Java.
    set "JAVAC_CMD=javac"
    set "JAVA_CMD=java"
)

for %%i in ("%~dp0..") do set "REPO_DIR=%%~fi"
set "SRC_DIR=%REPO_DIR%\src"
set "OUT_DIR=%REPO_DIR%\out\production\Othello-netprog"

echo Compiling...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

dir /s /b "%SRC_DIR%\model\*.java" "%SRC_DIR%\common\*.java" "%SRC_DIR%\client\*.java" > "%REPO_DIR%\sources.txt"

"%JAVAC_CMD%" -encoding UTF-8 -d "%OUT_DIR%" @"%REPO_DIR%\sources.txt"
del "%REPO_DIR%\sources.txt"

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

rem 画像リソースをコピー
xcopy /s /y /i "%SRC_DIR%\client\assets" "%OUT_DIR%\client\assets" > nul

echo Starting client...
"%JAVA_CMD%" -cp "%OUT_DIR%" client.Main

pause
