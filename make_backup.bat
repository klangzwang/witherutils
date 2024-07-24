RMDIR /S /Q runs
if not exist "runs" mkdir runs
if not exist "runs\client" mkdir runs\client
"C:\Program Files\7-Zip\7zG.exe" a "A:\dev\java\workspace1.21\Backup\Latest.zip" A:\dev\java\workspace1.21\WitherUtils -mx0 -xr!bin -xr!build -xr!.eclipse -xr!.gradle -xr!.settings