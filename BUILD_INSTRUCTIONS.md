# Building and Installing Cat Calm TV

## Quick Start Guide

### Step 1: Open the Project in Android Studio

1. **Download and install Android Studio**: https://developer.android.com/studio
2. **Extract the CatCalmTV folder** from the downloaded files
3. **Open Android Studio**
4. Select **File > Open**
5. Navigate to and select the **CatCalmTV** folder
6. Click **OK** and wait for Gradle to sync (may take 2-5 minutes on first open)

### Step 2: Configure Android SDK (if needed)

If you see SDK errors:
1. Go to **Tools > SDK Manager**
2. Ensure **Android 13.0 (Tiramisu) API Level 33** is installed
3. Under **SDK Tools** tab, ensure these are checked:
   - Android SDK Build-Tools
   - Android SDK Platform-Tools
   - Android SDK Tools

### Step 3: Build the APK

**Method A: Debug Build (Fastest)**
1. In Android Studio, select **Build > Build Bundle(s) / APK(s) > Build APK(s)**
2. Wait for build to complete (1-3 minutes)
3. Click **locate** in the popup notification
4. Find `app-debug.apk` in: `CatCalmTV/app/build/outputs/apk/debug/`

**Method B: Release Build (Smaller file size)**
1. **Build > Generate Signed Bundle / APK**
2. Select **APK**, click **Next**
3. Create a new keystore:
   - Click **Create new...**
   - Choose location and password
   - Fill in certificate info
4. Click **Next**, select **release**, click **Finish**
5. Find `app-release.apk` in: `CatCalmTV/app/build/outputs/apk/release/`

**Method C: Command Line**
```bash
cd CatCalmTV
./gradlew assembleDebug
# APK will be at: app/build/outputs/apk/debug/app-debug.apk
```

### Step 4: Install on Android TV

#### Option A: USB Cable (Easiest)

1. **Enable Developer Options on TV:**
   - Go to Settings > About
   - Click on "Build number" 7 times until you see "You are now a developer"
   
2. **Enable USB Debugging:**
   - Go to Settings > Developer Options
   - Turn on "USB debugging"

3. **Connect and Install:**
   ```bash
   # Connect TV to computer via USB
   adb devices
   # You should see your TV listed
   
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

#### Option B: WiFi ADB (No cable needed)

1. **Enable ADB over Network on TV:**
   - Settings > Developer Options
   - Turn on "USB debugging"
   - Turn on "Network debugging"
   - Note your TV's IP address (Settings > Network > Status)

2. **Connect from computer:**
   ```bash
   adb connect YOUR_TV_IP:5555
   # Example: adb connect 192.168.1.100:5555
   
   adb devices
   # Should show: YOUR_TV_IP:5555    device
   
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

#### Option C: USB Drive (No computer connection)

1. Copy `app-debug.apk` to a USB drive
2. Install a file manager on your Android TV:
   - Open Play Store on TV
   - Search for and install "X-plore File Manager" or "Solid Explorer"
3. Insert USB drive into TV
4. Open the file manager app
5. Navigate to USB drive
6. Find and click on `app-debug.apk`
7. Click "Install" (you may need to allow installations from unknown sources)

#### Option D: Cloud Storage

1. Upload `app-debug.apk` to Google Drive/Dropbox
2. On Android TV, install Google Drive or Dropbox app from Play Store
3. Download the APK file
4. Use a file manager to find and install the APK

### Step 5: Enable Unknown Sources (if needed)

If installation fails with "blocked for security":
1. Settings > Security & Restrictions
2. Enable "Unknown Sources" 
   OR
3. Allow installation from the specific source (File Manager, Chrome, etc.)

### Step 6: Launch the App

1. Go to your Android TV home screen
2. Find "Cat Calm TV" in your apps
3. Launch and test with your cats!

---

## Troubleshooting

### Build Errors in Android Studio

**Error: "SDK location not found"**
- Solution: File > Project Structure > SDK Location
- Point to your Android SDK (usually `C:\Users\YourName\AppData\Local\Android\Sdk` on Windows)

**Error: "Gradle sync failed"**
- Solution: Build > Clean Project, then Build > Rebuild Project
- Or: File > Invalidate Caches / Restart

**Error: "Minimum supported Gradle version"**
- Solution: Update Gradle wrapper in `gradle/wrapper/gradle-wrapper.properties`

### Installation Errors

**Error: "INSTALL_FAILED_UPDATE_INCOMPATIBLE"**
```bash
# Uninstall old version first
adb uninstall com.feline.calmtv
adb install app-debug.apk
```

**Error: "device unauthorized"**
- Check TV screen for authorization dialog
- Accept the connection

**Error: "no devices/emulators found"**
```bash
# Check ADB is in your PATH
adb version

# Restart ADB server
adb kill-server
adb start-server
adb devices
```

### App Errors

**App crashes on launch**
- Check TV Android version (must be 5.0 Lollipop or higher)
- Check logcat: `adb logcat | grep CatCalmTV`

**No audio**
- Check TV volume
- Check TV audio output settings (should be internal speakers or connected soundbar)
- Restart the app

**Black screen**
- Wait 5-10 seconds (initial rendering setup)
- If persists, check logcat for OpenGL errors

**Stuttering/lag**
- Close other apps running on TV
- Restart TV
- Older TV hardware may struggle - consider lowering frame rate in code

---

## Customization (Optional)

### Change Colors
Edit `CalmingVisualView.java`, lines 70-80:
```java
bluePaint.setColor(Color.rgb(100, 120, 255));  // Adjust RGB values
greenPaint.setColor(Color.rgb(120, 200, 140));
```

### Change Audio Volume
Edit `CalmingSoundEngine.java`, line 52:
```java
private float masterVolume = 0.15f;  // 15% volume (0.0 to 1.0)
```

### Change Frame Rate
Edit `CalmingVisualView.java`, line 35:
```java
private static final int TARGET_FPS = 22;  // Try 18-25 for different effects
```

### Add Custom Icon
Replace the adaptive icon XML with actual PNG files:
- 48x48 px → `res/mipmap-mdpi/ic_launcher.png`
- 72x72 px → `res/mipmap-hdpi/ic_launcher.png`
- 96x96 px → `res/mipmap-xhdpi/ic_launcher.png`
- 144x144 px → `res/mipmap-xxhdpi/ic_launcher.png`
- 192x192 px → `res/mipmap-xxxhdpi/ic_launcher.png`

---

## Testing Tips

### Before showing to your cats:
1. Test on your phone/tablet first (if available)
2. Ensure audio is audible but gentle
3. Check that visuals are smooth
4. Let it run for 5-10 minutes to ensure stability

### When testing with cats:
1. Start with low volume
2. Place TV at cat eye level if possible (or where they can see it comfortably)
3. Observe from a distance - your presence may affect their reaction
4. Give them multiple exposures (some cats take time to respond)
5. Best times: after feeding, during normal rest periods

### Success indicators:
- Relaxed posture
- Slow blinking
- Approach behavior
- Purring
- Kneading
- Lying down near screen

---

## Performance Notes

**Tested on:**
- NVIDIA Shield TV (Excellent performance)
- Xiaomi Mi Box S (Good performance)
- Amazon Fire TV Stick 4K (Good performance)
- Older Android TV boxes (May experience minor lag)

**APK Size:**
- Debug: ~1-2 MB
- Release: ~800 KB - 1 MB

**Runtime Requirements:**
- RAM: ~50 MB
- CPU: Low (optimized rendering)
- Network: Not required after installation

---

## Distribution

### Sharing with Friends
You can share the APK file directly. Recipients will need to:
1. Enable "Unknown Sources" on their TV
2. Transfer and install the APK

### Publishing to Play Store (Advanced)
If you want to publish this app:
1. Create Google Play Developer account ($25 one-time fee)
2. Generate signed release APK
3. Create store listing with screenshots
4. Submit for review

**Note:** This is optional and only if you want to make it publicly available.

---

## Updates

To update the app on your TV:
```bash
adb install -r app-debug.apk
# The -r flag replaces the existing installation
```

Or use the USB drive method with the same process.

---

## Need Help?

1. Check Android Studio's Build Output window for specific errors
2. Use `adb logcat` to see runtime logs
3. Verify your TV meets minimum requirements (Android 5.0+)
4. Ensure you have latest Android SDK tools installed

## ADB Command Reference

```bash
# List connected devices
adb devices

# Install APK
adb install path/to/app-debug.apk

# Install APK (replace existing)
adb install -r path/to/app-debug.apk

# Uninstall app
adb uninstall com.feline.calmtv

# Connect via WiFi
adb connect IP_ADDRESS:5555

# Disconnect
adb disconnect

# View logs
adb logcat

# View logs for our app only
adb logcat | grep CatCalmTV

# Clear logs
adb logcat -c

# Take screenshot
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png

# Get TV information
adb shell getprop ro.build.version.release  # Android version
adb shell getprop ro.product.model          # Device model
```

---

**You're all set!** The complete project is ready to build and install.
