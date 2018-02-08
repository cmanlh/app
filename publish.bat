set TOMCAT_WEB=%TOMCAT%\webapps
set CATALINA_HOME=%TOMCAT%
set SYS_ROOT=.\app-example\app-example-%2
set SYS_RELEASE=.\%SYS_ROOT%\target
set SYS_NAME=%2

if "%1" == "stop" goto stopTomcat
if "%1" == "restart" goto stopTomcat
if "%1" == "push" goto pushResources
if "%1" == "debug" goto debugTomcat
goto publish

:pushResources
xcopy /S /Y %SYS_ROOT%\WebContent\resources\* %TOMCAT_WEB%\%SYS_NAME%\resources\
goto end

:stopTomcat
rem to stop tomcat
call %TOMCAT%\bin\shutdown.bat

:publish
rd /S /Q %TOMCAT_WEB%\%SYS_NAME%
xcopy /S %SYS_RELEASE%\%SYS_NAME%\* %TOMCAT_WEB%\%SYS_NAME%\
if "%1" == "start" goto startTomcat
if "%1" == "restart" goto startTomcat
goto end

:startTomcat
%TOMCAT%\bin\startup.bat
goto end

:debugTomcat
%TOMCAT%\bin\catalina.bat jpda start

:end
