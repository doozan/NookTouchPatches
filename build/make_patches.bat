@ECHO OFF

set VER=1.2.0

cd "%~dp0\smali\%VER%\android.policy"
call git diff base  > ..\android.policy.patch

cd "%~dp0\smali\%VER%\services"
call git diff base  > ..\services.patch
