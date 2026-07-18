@echo off
title EBTPS Banking Application
echo Starting Electronic Banking Transaction Processing System (EBTPS)...
echo Connecting to Supabase Cloud Database...
echo.

java -cp "bin;postgresql.jar" com.ebtps.main.Application

echo.
pause
