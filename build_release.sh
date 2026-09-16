#!/usr/bin/env bash
set -e

echo "===================================================="
echo " Building Dawah to Jannah Android APK for Release   "
echo "===================================================="

# Decode keystore if needed
if [ -f "debug.keystore.base64" ] && [ ! -f "debug.keystore" ]; then
  echo "Decoding debug.keystore from base64..."
  base64 -d debug.keystore.base64 > debug.keystore
fi

# Build APK
gradle assembleDebug --no-daemon

# Create staging directory
mkdir -p release_artifacts
APK_PATH=$(find app/build/outputs/apk -name "*.apk" | head -n 1)

if [ -n "$APK_PATH" ]; then
  cp "$APK_PATH" release_artifacts/app-release.apk
  cp "$APK_PATH" release_artifacts/app-debug.apk
  echo ""
  echo "✅ APK বিল্ড সম্পন্ন হয়েছে!"
  echo "ফাইলের অবস্থান: release_artifacts/app-release.apk"
  ls -lh release_artifacts/app-release.apk
  echo ""
  echo "গিটহাবে রিলিজ আপলোড করতে:"
  echo "  gh release create latest release_artifacts/app-release.apk --clobber"
else
  echo "❌ APK ফাইল খুঁজে পাওয়া যায়নি।"
  exit 1
fi
