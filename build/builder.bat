@echo off

IF "%1"=="" goto error

SET VER=%1
SET MAINDIR=%~dp0
SET CLASSES_DEX=%MAINDIR%\..\bin\classes.dex
SET SZIP=%MAINDIR%\bin\7za.exe
SET SMALI=%MAINDIR%\bin\smali-1.4.0.jar
SET BAKSMALI=%MAINDIR%\bin\baksmali-1.4.0.jar

echo "decompiling modutils"
java -jar "%BAKSMALI%" "%CLASSES_DEX%" -output "%MAINDIR%\modutils"
if errorlevel 1 goto err

echo "inserting modutils into package sources"
del "%MAINDIR%\smali\%VER%\android.policy\com\android\internal\policy\impl\ModUtils*.smali"
copy "%MAINDIR%\modutils\com\android\internal\policy\impl\ModUtils*.smali" "%MAINDIR%\smali\%VER%\android.policy\com\android\internal\policy\impl"
del "%MAINDIR%\smali\%VER%\services\com\android\server\status\ModUtils*.smali"
copy "%MAINDIR%\modutils\com\android\server\status\ModUtils*.smali"       "%MAINDIR%\smali\%VER%\services\com\android\server\status"


echo building android.policy.jar
java -jar "%SMALI%" -o "%MAINDIR%\jar\%VER%\android.policy\classes.dex" "%MAINDIR%\smali\%VER%\android.policy"
if errorlevel 1 goto err

del "%MAINDIR%\jar\%VER%\android.policy.jar"
cd "%MAINDIR%\jar\%VER%\android.policy"
"%SZIP%" a -tzip "%MAINDIR%\jar\%VER%\android.policy.jar" * > NUL
if errorlevel 1 goto err
cd "%MAINDIR%"



echo building services.jar
java -jar "%SMALI%" -o "%MAINDIR%\jar\%VER%\services\classes.dex" "%MAINDIR%\smali\%VER%\services"
if errorlevel 1 goto err

del "%MAINDIR%\jar\%VER%\services.jar"
cd "%MAINDIR%\jar\%VER%\services"
"%SZIP%" a -tzip "%MAINDIR%\jar\%VER%\services.jar" *  > NUL
if errorlevel 1 goto error
cd "%MAINDIR%"





rem  disable usb drive automount when nook restarts
adb shell setprop persist.service.mount.umsauto 0

adb shell mount -o rw,remount -t ext2 /dev/block/mmcblk0p5 /system
adb push "%MAINDIR%\jar\%VER%\android.policy.jar" system/framework/
adb push "%MAINDIR%\jar\%VER%\services.jar" system/framework/

rem killall sevicemanager is faster than adb reboot
adb shell killall servicemanager
echo Servicemanager is restarting
goto done

:error
echo Error

:done