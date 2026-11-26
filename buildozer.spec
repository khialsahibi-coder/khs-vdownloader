[app]
title = KHS VDownloader
package.name = khsvdownloader
package.domain = khs.vdownloader
source.dir = .
source.include_exts = py,kv,png,jpg,at,txt,xml,java
version = 1.0.0

requirements = python3,kivy,yt-dlp,ffmpeg

android.add_src = app/src/main/java
android.activity_class_name = com.khs.vdownloader.MainActivity
p4a.bootstrap = sdl2

android.permissions = WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,FOREGROUND_SERVICE,INTERNET,ACCESS_NETWORK_STATE

android.api = 21.1.2
android.minapi = 21
android.ndk = 25b
android.ndk_api = 21
android.archs = arm64-v8a, armeabi-v7a

p4a.fork = kivy
p4a.branch = master

log_level = 2
warn_on_root = 1
buildozer.spec_version = 1.0
