@echo off
cls

RM c:\windows\temp\80939fn9v3992
MD c:\windows\temp\80939fn9v3992

robocopy A:\PrismLauncher\instances\1.20.1\.minecraft\mods "a:\server\mods" *.* /mt /z
robocopy A:\PrismLauncher\instances\1.20.1(1)\.minecraft\mods "a:\server\mods" *.* /mt /z
robocopy A:\server\mods "c:\windows\temp\80939fn9v3992" *.* /mt /z
robocopy A:\dev\java\workspace1.20.1\WitherUtils\build\libs "c:\windows\temp\80939fn9v3992" *.* /mt /z

del /q "A:\server\mods\*.*"
del /q "A:\PrismLauncher\instances\1.20.1\.minecraft\mods\*.*"
del /q "A:\PrismLauncher\instances\1.20.1(1)\.minecraft\mods\*.*"

timeout /t 1 /nobreak > NUL

robocopy c:\windows\temp\80939fn9v3992 "A:\server\mods" *.* /mt /z
robocopy c:\windows\temp\80939fn9v3992 "a:\PrismLauncher\instances\1.20.1\.minecraft\mods" *.* /mt /z
robocopy c:\windows\temp\80939fn9v3992 "a:\PrismLauncher\instances\1.20.1(1)\.minecraft\mods" *.* /mt /z

timeout /t 1 /nobreak > NUL

Start cmd /c "A:\server\run.lnk"

timeout /t 1 /nobreak > NUL

Start A:\PrismLauncher\prismlauncher.exe --profile geni --launch 1.20.1
Start A:\PrismLauncher\prismlauncher.exe --profile fotzi --launch 1.20.1(1)

timeout /t 60 /nobreak > NUL

cmdow "Minecraft Forge* 1.20.1" /REN "Player1"
cmdow "Player1" /SIZ 960 540
cmdow "Player1" /MOV 0 0

cmdow "Minecraft Forge* 1.20.1" /REN "Player2"
cmdow "Player2" /SIZ 960 540
cmdow "Player2" /MOV 960 0