@echo off
if exist %~dp0jre\ (
  set JAVA="%~dp0jre\bin\java.exe"
) else (
  set JAVA="%JAVA_HOME%\bin\java.exe"
)
%JAVA% -cp "%~dp0libs\*" ch.rweiss.seltec.taf3.export.SeltecTaf3Export %*