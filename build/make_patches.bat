@ECHO OFF

set VER=1.2.0

cd "%~dp0\smali\%VER%\android.policy"
call git diff base --src-prefix=a/android.policy/ --dst-prefix=b/android.policy/ > "%~dp0..\patches\%VER%\android.policy.patch"
"%~dp0\bin\unix2dos" "%~dp0..\patches\%VER%\android.policy.patch"

cd "%~dp0\smali\%VER%\services"
call git diff base --src-prefix=a/services/ --dst-prefix=b/services/  > "%~dp0..\patches\%VER%\services.patch"
"%~dp0\bin\unix2dos" "%~dp0..\patches\%VER%\services.patch"

cd "%~dp0"
