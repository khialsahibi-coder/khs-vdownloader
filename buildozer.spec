# buildozer.spec - FIXED VERSION (key additions)

[app]
title = KHS VDownloader
package.name = khsvdownloader
package.domain = khs.vdownloader
source.dir = .
source.include_exts = py,kv,png,jpg,at,txt,xml,java
version = 1.0.0

# Requirements - Python packages to include
requirements = python3,kivy,yt-dlp,ffmpeg

# CRITICAL: Add Java sources to the build
android.add_src = app/src/main/java

# CRITICAL: Set the main activity class (Java, not Python)
android.activity_class_name = com.khs.vdownloader.MainActivity

# Use sdl2 bootstrap for Java+Python hybrid apps
p4a.bootstrap = sdl2

# Permissions
android.permissions = WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,FOREGROUND_SERVICE,INTERNET,ACCESS_NETWORK_STATE

# Android config
android.api = 33
android.minapi = 21
android.ndk = 25b
android.ndk_api = 21
android.private_storage = True
android.archs = arm64-v8a, armeabi-v7a

# Python-for-android config
p4a.fork = kivy
p4a.branch = master

# Buildozer
log_level = 2
warn_on_root = 1
buildozer.spec_version = 1.0