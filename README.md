# KHS VDownloader

Android video downloader app supporting YouTube, Facebook, TikTok, and 1000+ sites via yt-dlp.

## Features
- Download videos from YouTube, Facebook, TikTok, and many more
- Multiple quality options (1080p, 720p, 480p, 360p, audio only)
- Background downloads with progress notifications
- Download history
- Custom save location

## Building APK

### Using GitHub Actions (Recommended)
1. Fork this repository
2. Go to Actions tab
3. Run "Build KHS VDownloader APK" workflow
4. Download APK from artifacts

### Local Build (Linux/macOS/WSL)
```bash
# Install dependencies
pip install buildozer cython==0.29.36

# Build debug APK
buildozer -v android debug

# Build release APK
buildozer -v android release
```

## Project Structure
```
KHSVDownloader/
├── app/
│   └── src/main/
│       ├── java/com/khs/vdownloader/
│       │   ├── MainActivity.java       # Main UI
│       │   ├── DownloadService.java    # Foreground download service
│       │   ├── DownloadManager.java    # Core download logic
│       │   ├── FormatSelectionDialog.java
│       │   └── DownloadAdapter.java    # History RecyclerView
│       ├── res/
│       │   ├── layout/
│       │   └── values/
│       └── AndroidManifest.xml
├── .github/workflows/
│   └── build-apk.yml                 # GitHub Actions CI/CD
├── buildozer.spec                    # Buildozer configuration
├── main.py                           # Python entry point
└── .gitignore
```

## Requirements
- Android 5.0+ (API 21)
- Internet permission
- Storage permission

## License
Proprietary - KHS VDownloader