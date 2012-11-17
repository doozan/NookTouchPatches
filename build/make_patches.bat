@ECHO OFF

set VER=1.1.5

cd "%~dp0\smali\%VER%\android.policy"
call git diff base  > ..\android.policy.patch
"%~dp0\bin\unix2dos" ..\android.policy.patch

cd "%~dp0\smali\%VER%\services"
call git diff base  > ..\services.patch
"%~dp0\bin\unix2dos" ..\services.patch


set VER=1.2.0

cd "%~dp0\smali\%VER%\android.policy"
call git diff base  > ..\android.policy.patch
"%~dp0\bin\unix2dos" ..\android.policy.patch

cd "%~dp0\smali\%VER%\services"
call git diff base  > ..\services.patch
"%~dp0\bin\unix2dos" ..\services.patch
