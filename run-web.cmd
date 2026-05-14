@echo off
setlocal EnableExtensions EnableDelayedExpansion
cd /d "%~dp0"

chcp 65001 >nul 2>&1

if defined JAVA_HOME goto :have_home

for /f "usebackq delims=" %%i in (`powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\detect-java-home.ps1"`) do (
  set "JAVA_HOME=%%i"
)

if not defined JAVA_HOME goto :no_java_home

:have_home
if not defined JAVA_HOME goto :no_java_home

for /f "delims=" %%A in ("!JAVA_HOME!") do set "JAVA_HOME=%%~A"

if exist "!JAVA_HOME!\bin\java.exe" goto :java_ok
if exist "!JAVA_HOME!\bin\java" goto :java_ok

echo.
echo [ERROR] JAVA_HOME is invalid: no java.exe under bin\
echo   JAVA_HOME=!JAVA_HOME!
echo.
echo Fix: set JAVA_HOME to your JDK folder, then run again.
echo.
pause
exit /b 1

:no_java_home
echo.
echo [ERROR] Could not detect JAVA_HOME.
echo Fix: set "JAVA_HOME=...your JDK root..." then run this script again.
echo.
pause
exit /b 1

:java_ok
echo Using JAVA_HOME=!JAVA_HOME!
echo Starting Jetty (Servlet WAR). First run may download Maven + deps.
echo Open: http://localhost:8080/   (port from pom.xml property jetty.http.port)
echo If 8080 busy: .\run-web.cmd -Djetty.http.port=8081
echo.

call "%~dp0mvnw.cmd" jetty:run %*
if errorlevel 1 (
  echo.
  echo [ERROR] jetty:run failed (network, port in use, etc.)
  pause
)
endlocal
exit /b 0
