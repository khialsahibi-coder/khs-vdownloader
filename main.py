#!/usr/bin/env python
"""
KHS VDownloader - Main Python entry point for buildozer
This is a minimal Kivy app that bridges to the Java Android app
"""

import os
import sys

# This is a placeholder - the actual Android app is Java-based
# buildozer requires a main.py, but our app uses Java activities
# The Python side will be invoked via pyjnius from Java for yt-dlp operations

from kivy.app import App
from kivy.uix.label import Label

class KHSVDownloaderApp(App):
    def build(self):
        return Label(text='KHS VDownloader\nPython bridge for yt-dlp')

if __name__ == '__main__':
    KHSVDownloaderApp().run()