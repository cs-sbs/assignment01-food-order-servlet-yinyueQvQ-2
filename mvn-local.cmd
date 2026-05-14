@echo off
REM Clears JAVA_HOME for this invocation only. Fixes mvn.cmd parse errors when
REM JAVA_HOME has spaces or stray quotes (CMD "此时不应有 ... ""==""" errors).
set "JAVA_HOME="
cd /d "%~dp0"
call "%~dp0mvnw.cmd" %*
