@echo off
title CineVerse Backend - Server
echo ================================
echo   Iniciando servidor CineVerse
echo ================================
echo.

REM Moverse desde Arranque_Instaladores a springboot\cineverse\target
cd /d ..\springboot\cineverse\target

java -jar cineverse-0.0.1-SNAPSHOT.jar

echo.
echo ================================
echo   Servidor detenido
echo ================================
pause