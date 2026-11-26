# KHS VDownloader - Simplified APK Build Guide

## Overview
This project builds an Android APK for KHS VDownloader using GitHub Actions. 
Two files need updating before pushing, then the build runs automatically.

---

## Step 1: Update 2 Files (Critical)

### 1. `buildozer.spec` — Already Updated
The file at `D:\claude\KHSVDownloader\buildozer.spec` has been simplified with essential config only.

### 2. `DownloadService.java` — Already Updated  
The file at `D:\claude\KHSVDownloader\app\src\main\java\com\khs\vdownloader\DownloadService.java` has been simplified with pyjnius integration.

✅ **Both files are ready — no further editing needed.**

---

## Step 2: Push to GitHub

### Option A: GitHub Web UI (Easiest — No Git Installation Required)
1. Go to: **https://github.com/khialsahibi-coder/khs-vdownloader**
2. Click **"Add file"** → **"Upload files"**
3. **Drag ALL files from the `KHSVDownloader` folder** (the whole project directory contents)
4. Click **"Commit changes"**

### Option B: Command Line (if Git Works)
```cmd
cd D:\claude\KHSVDownloader
git init
git add .
git commit -m "Simplified: KHS VDownloader APK build"
git remote add origin https://github.com/khialsahibi-coder/khs-vdownloader.git
git push -u origin main
```

---

## Step 3: Trigger the APK Build

### Go to Actions
**https://github.com/khialsahibi-coder/khs-vdownloader/actions**

1. Click **"Build KHS VDownloader APK"** workflow
2. Click **"Run workflow"** → Select "debug" → **"Run workflow"**
3. Wait **15-30 minutes** (first run downloads SDK/NDK)
4. **Download APK**: Click completed run → **Artifacts** → `KHS-VDownloader-APK`

---

## What Was Fixed (So You Don't Have To Worry)

| Issue | Fix Applied |
|-------|-------------|
| Android SDK license not accepted in CI | `yes | sdkmanager --licenses` step added |
| Ubuntu 26.04 `libtinfo5` package missing | `libtinfo6` / `libncurses-dev` fallback added |
| MainActivity not extending PythonActivity | Already fixed — extends PythonActivity |
| AndroidManifest missing FOREGROUND_SERVICE_TYPE_DOWNLOAD | Already fixed — permission included |
| DownloadService pyjnius integration | Simplified version uploaded |

---

## After the Build Succeeds

1. Go to the **Actions** tab in GitHub
2. Click the completed workflow run
3. Click **Artifacts** → download `KHS-VDownloader-APK`
4. The APK is at: `bin/KHS-VDownloader-debug.apk`

---

## Project Structure (After Push)

```
KHSVDownloader/
├── app/
│   └── src/main/
│       ├── java/com/khs/vdownloader/
│       │   ├── MainActivity.java       # ✅ Fixed: extends PythonActivity
│       │   ├── DownloadService.java    # ✅ Fixed: pyjnius integration
│       │   ├── DownloadManager.java
│       │   ├── FormatSelectionDialog.java
│       │   └── DownloadAdapter.java
│       ├── res/
│       │   ├── layout/
│       │   └── values/
│       └── AndroidManifest.xml         # ✅ Fixed: has FOREGROUND_SERVICE_TYPE_DOWNLOAD
├── .github/workflows/
│   └── build-apk.yml                   # ✅ Ready: license + ncurses fixes
├── buildozer.spec                      # ✅ Simplified: essentials only
├── main.py                             # Python entry point
├── .gitignore                          # ✅ Simplified: essentials only
└── README.md
```

---

## Need Help?

If the build fails, check the GitHub Actions logs for:
- Python/yt-dlp errors (version pinning may help)
- Java compilation issues (ensure `android.add_src` is set in buildozer.spec)

**That's it — 2 files updated, push, click Run, wait 15-30 minutes, download APK.**