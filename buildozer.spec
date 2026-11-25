# buildozer.spec - WITH PINNED VERSIONS

[app]
title = KHS VDownloader
package.name = khsvdownloader
package.domain = khs.vdownloader
source.dir = .
source.include_exts = py,kv,png,jpg,at,txt,xml,java
version = 1.0.0

# CRITICAL: Pin versions for buildozer compatibility
# Python 3.10 required (3.11+ breaks buildozer)
# cython 0.29.36 required (newer breaks buildozer)
# yt-dlp pinned to avoid conflicts
requirements = python3==3.10.*,kivy,yt-dlp==2024.1.1,ffmpeg

# CRITICAL: Include Java sources
android.add_src = app/src/main/java
android.activity_class_name = com.khs.vdownloader.MainActivity
p4a.bootstrap = sdl2

# Permissions (added FOREGROUND_SERVICE_TYPE_DOWNLOAD)
android.permissions = WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,FOREGROUND_SERVICE,FOREGROUND_SERVICE_TYPE_DOWNLOAD,INTERNET,ACCESS_NETWORK_STATE

# Android config
android.api = 33
android.minapi = 21
android.ndk = 25b
android.ndk_api = 21
android.private_storage = True
android.archs = arm64-v8a, armeabi-v7a

# Python-for-android
p4a.fork = kivy
p4a.branch = master

# Buildozer
log_level = 2
warn_on_root = 1
buildozer.spec_version = 1.0
