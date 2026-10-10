@REM ----------------------------------------------------------------------------
@REM Maven Wrapper Script for Smart Campus Placements Backend
@REM ----------------------------------------------------------------------------
@echo off
setlocal

set "MAVEN_CMD=C:\tools\apache-maven-3.9.9\bin\mvn.cmd"
if exist "%MAVEN_CMD%" (
    call "%MAVEN_CMD%" %*
) else (
    where mvn >nul 2>nul
    if %ERRORLEVEL% equ 0 (
        call mvn %*
    ) else (
        echo Error: Apache Maven was not found in C:\tools\apache-maven-3.9.9\bin\mvn.cmd or in PATH.
        exit /b 1
    )
)
