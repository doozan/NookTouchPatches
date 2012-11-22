Patch build/development instructions are available [below](#patch-development). 

<a name="install"></a>
Patch Installation
==================

The current patches are only compatible with Nook system software 1.2.0 and 1.1.5 (Glowtouch original firmware).

Configure your build directory
------------------------------
create a new "nookmods" folder

download [patche01.zip](http://geoffair.net/projects/zips/patche01.zip) and unzip patch.exe to the "nookmods" folder

download [7za920.zip](http://sourceforge.net/projects/sevenzip/files/7-Zip/9.20/7za920.zip/download?use_mirror=iweb) and extract 7za.exe to the "nookmods" folder

download and save the following files to the nookmods folder:
[baksmali](https://smali.googlecode.com/files/baksmali-1.4.0.jar),
[smali](https://smali.googlecode.com/files/smali-1.4.0.jar)

Download and save the appropriate patches to the nookmods folder.

For system 1.2.0, use [android.policy.patch](https://github.com/doozan/NookTouchPatches/raw/master/patches/1.2.0/android.policy.patch) and
[services.patch](https://github.com/doozan/NookTouchPatches/raw/master/patches/1.2.0/services.patch)

For system 1.1.5, use [android.policy.patch](https://github.com/doozan/NookTouchPatches/raw/master/patches/1.1.5/android.policy.patch) and
[services.patch](https://github.com/doozan/NookTouchPatches/raw/master/patches/1.1.5/services.patch)



Decompile, patch, recompile
---------------------------

    cd nookmods

### Extract and patch android.policy.jar
    adb pull /system/framework/android.policy.jar android.policy.orig.jar
    java -jar baksmali-1.4.0.jar -o android.policy android.policy.orig.jar
    patch -p1 < android.policy.patch

### Compile new android.policy.jar
    7za.exe e android.policy.orig.jar  -oandroid.policy-bin
    java -jar smali-1.4.0.jar -o android.policy-bin\classes.dex android.policy
    cd android.policy-bin
    ..\7za.exe a -mx9 -tzip ..\android.policy.jar *
    cd ..


### Extract and patch services.jar
    adb pull /system/framework/services.jar services.orig.jar
    java -jar baksmali-1.4.0.jar -o services services.orig.jar
    patch -p1 < services.patch

### Compile new services.jar
    7za.exe e services.orig.jar  -oservices-bin
    java -jar smali-1.4.0.jar -o services-bin\classes.dex services
    cd services-bin
    ..\7za.exe a -mx9 -tzip ..\services.jar *
    cd ..

Installation
------------
    adb shell mount -o rw,remount -t ext2 /dev/block/mmcblk0p5 /system
    adb push android.policy.jar /system/framework/
    adb push services.jar /system/framework/
    adb reboot

After your nook reboots, you can install the [Nook Touch Mod Manager](https://github.com/doozan/NookTouchModManager/downloads) to configure the mods.


Output from the above commands
------------------------------

    D:\>cd nookmod

    D:\nookmod>dir
     Volume in drive D has no label.

     Directory of D:\nookmod

    11/14/2012  01:07 AM    <DIR>          .
    11/14/2012  01:07 AM    <DIR>          ..
    11/18/2010  11:27 AM           587,776 7za.exe
    11/14/2012  12:38 AM             9,192 android.policy.patch
    11/05/2012  10:20 AM           524,051 baksmali-1.4.0.jar
    06/04/2010  12:48 PM           176,128 patch.exe
    11/14/2012  12:38 AM            57,910 services.patch
    11/05/2012  10:21 AM           693,227 smali-1.4.0.jar
                   7 File(s)      3,168,844 bytes
                   2 Dir(s)  45,238,874,112 bytes free

    D:\nookmod>adb pull /system/framework/android.policy.jar android.policy.orig.jar

    D:\nookmod>java -jar baksmali-1.4.0.jar -o android.policy android.policy.orig.jar

    D:\nookmod>patch -p1 < android.policy.patch
    patching file android.policy/com/android/internal/policy/impl/LockScreen.smali
    patching file android.policy/com/android/internal/policy/impl/ModUtils.smali
    patching file android.policy/com/android/internal/policy/impl/PhoneWindowManager.smali

    D:\nookmod>7za.exe e android.policy.orig.jar  -oandroid.policy-bin

    7-Zip (A) 9.20  Copyright (c) 1999-2010 Igor Pavlov  2010-11-18

    Processing archive: android.policy.orig.jar

    Extracting  META-INF
    Extracting  META-INF\MANIFEST.MF
    Extracting  classes.dex

    Everything is Ok

    Folders: 1
    Files: 2
    Size:       176807
    Compressed: 82207

    D:\nookmod>java -jar smali-1.4.0.jar -o android.policy-bin\classes.dex android.policy

    D:\nookmod>cd android.policy-bin

    D:\nookmod\android.policy-bin>..\7za.exe a -mx9 -tzip ..\android.policy.jar *

    7-Zip (A) 9.20  Copyright (c) 1999-2010 Igor Pavlov  2010-11-18

    Scanning

    Updating archive ..\android.policy.jar

    Compressing  classes.dex
    Compressing  MANIFEST.MF

    Everything is Ok

    D:\nookmod\android.policy-bin>cd ..

    D:\nookmod>adb pull /system/framework/services.jar services.orig.jar
    error: device not found

    D:\nookmod>java -jar baksmali-1.4.0.jar -o services services.orig.jar

    D:\nookmod>patch -p1 < services.patch
    patching file services/com/android/server/status/BNGossamerQuickNavbar.smali
    patching file services/com/android/server/status/ModUtils$1.smali
    patching file services/com/android/server/status/ModUtils.smali
    patching file services/com/android/server/status/StatusBarService.smali

    D:\nookmod>7za.exe e services.orig.jar  -oservices-bin

    7-Zip (A) 9.20  Copyright (c) 1999-2010 Igor Pavlov  2010-11-18

    Processing archive: services.orig.jar

    Extracting  META-INF
    Extracting  META-INF\MANIFEST.MF
    Extracting  classes.dex

    Everything is Ok

    Folders: 1
    Files: 2
    Size:       1111087
    Compressed: 515208

    D:\nookmod>java -jar smali-1.4.0.jar -o services-bin\classes.dex services

    D:\nookmod>cd services-bin

    D:\nookmod\services-bin>..\7za.exe a -mx9 -tzip ..\services.jar *

    7-Zip (A) 9.20  Copyright (c) 1999-2010 Igor Pavlov  2010-11-18

    Scanning

    Updating archive ..\services.jar

    Compressing  classes.dex
    Compressing  MANIFEST.MF

    Everything is Ok

    D:\nookmod\services-bin>cd ..

    D:\nookmod>dir
     Volume in drive D has no label.

     Directory of D:\nookmod

    11/14/2012  01:12 AM    <DIR>          .
    11/14/2012  01:12 AM    <DIR>          ..
    11/18/2010  11:27 AM           587,776 7za.exe
    11/14/2012  01:08 AM           523,145 android-policy.jar
    11/14/2012  01:07 AM    <DIR>          android.policy
    11/14/2012  01:08 AM    <DIR>          android.policy-bin
    11/14/2012  01:12 AM            81,793 android.policy.jar
    10/13/2010  01:29 PM            82,207 android.policy.orig.jar
    11/14/2012  12:38 AM             9,192 android.policy.patch
    11/05/2012  10:20 AM           524,051 baksmali-1.4.0.jar
    06/04/2010  12:48 PM           176,128 patch.exe
    11/14/2012  01:08 AM    <DIR>          services
    11/14/2012  01:08 AM    <DIR>          services-bin
    11/14/2012  01:12 AM           523,145 services.jar
    10/13/2010  01:29 PM           515,208 services.orig.jar
    11/14/2012  12:38 AM            57,910 services.patch
    11/05/2012  10:21 AM           693,227 smali-1.4.0.jar
                  11 File(s)      3,773,782 bytes
                   6 Dir(s)  45,228,691,456 bytes free



Patch development
=================

To develop and test patches, you'll need to import the project into eclipse and configure and external build command to build the jars and copy them to your nook.  You should also install [msysGit](http://msysgit.github.com/) to manage revisions of your smali patches.


Import project into eclipse
---------------------------
File | Import

Git | Projects from Git

URI

URI: https://github.com/doozan/NookTouchPatches.git

Branch Selection: check master

Destination: Leave defaults or set custom path

Use the New Project wizard

Android | Android Project from Existing Code

Root Directory: Browse to folder you specified for the destination


Configure the jar builder
-------------------------
download [7za920.zip](http://sourceforge.net/projects/sevenzip/files/7-Zip/9.20/7za920.zip/download?use_mirror=iweb) and extract 7za.exe to the "build\bin" folder

download and save the the following files to the "build\bin" folder:
[baksmali](https://smali.googlecode.com/files/baksmali-1.4.0.jar),
[smali](https://smali.googlecode.com/files/smali-1.4.0.jar),

### Extract and decompile the jars
Export VER to match the platform you're targetting (1.2.0 or 1.1.5)

    set VER=1.2.0
    cd build

    adb pull /system/framework/android.policy.jar jar/%VER%/android.policy.orig.jar
    bin\7za.exe e -ojar\%VER%\android.policy jar\%VER%\android.policy.orig.jar
    java -jar bin\baksmali-1.4.0.jar -o smali/%VER%/android.policy jar/%VER%/android.policy.orig.jar
    cd smali\%VER%\android.policy
    git init
    git add *
    git commit -m "Initial commit"
    git tag -a base -m "Original source"
    cd ..\..\..

    adb pull /system/framework/services.jar jar/%VER%/services.orig.jar
    bin\7za.exe e -ojar\%VER%\services jar\%VER%\services.orig.jar
    java -jar bin\baksmali-1.4.0.jar -o smali/%VER%/services jar/%VER%/services.orig.jar
    cd smali\%VER%\services
    git init
    git add *
    git commit -m "Initial commit"
    git tag -a base -m "Original source"
    cd ..\..\..


Configure the builder in Eclipse
--------------------------------

From the Eclipse menu, select Project | Properties

From the tree on the left, select Builders, click New and select Program

Name: BuildJars

Location: Click Browse Workspace, select builder.bat in your "build" folder

Working Directory: Click Browse Workspace, select your "build" folder

Arguments: The system version you are targeting (ex 1.2.0 or 1.1.5).

Build options: Run the builder: During manual builds

Check Specify working set of relavent resources and click the Specify Resources button

Check the "src" directory and the "build\smali" directory under the NookTouchPatches project

Save


From the menu, select Windows | Preferences

From the tree, choose Android | Build

uncheck "Skip packaging and dexing until export or launch. (Speeds up automatic builds on file save)"


Configure the Run action
------------------------

From the Eclipse menu, select Run | Run Configurations

Right click Android Application and choose "New"

Name: NookTouchPatches

Project: click browse, select NookTouchPatches

Launch Action: Do Nothing


Try it out
----------

You can now edit the ModUtils.java files in eclipse, as well as the smali files in build\smali\<version>.  When you're ready to try out your patches, click the run button.  The jars will automatically be built and copied to you nook and android will be restarted to use the new packages.