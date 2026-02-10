# ULTIMATE FIX - For Persistent Gradle Errors

## You're Getting Repeated Gradle Errors?

This is a **CLEAN, SIMPLIFIED** version that should work even if you're having persistent issues.

## What's Different in This Version

1. ✅ **Simplified Gradle setup** - removed complex dependency resolution
2. ✅ **Newer Gradle 8.2** - better compatibility
3. ✅ **Plugin version 8.2.0** - latest stable
4. ✅ **Longer network timeout** - 60 seconds instead of default
5. ✅ **Proper repository order** - Google first, then Maven

## CLEAN INSTALL - Step by Step

### Step 1: Complete Clean Slate

**Before opening in Android Studio**, do this:

1. **Delete your Gradle cache completely:**

   **On Mac:**
   ```bash
   rm -rf ~/.gradle/caches/
   rm -rf ~/.gradle/wrapper/
   rm -rf ~/.android/build-cache/
   ```

   **On Windows (PowerShell):**
   ```powershell
   Remove-Item -Recurse -Force $env:USERPROFILE\.gradle\caches
   Remove-Item -Recurse -Force $env:USERPROFILE\.gradle\wrapper
   Remove-Item -Recurse -Force $env:USERPROFILE\.android\build-cache
   ```

2. **Extract the NEW zip file** (CatCalmTV-CLEAN.zip)

3. **Make sure you have good internet** - stable connection required

### Step 2: Open in Android Studio

1. Open Android Studio
2. **Close any other projects first**
3. File > Open
4. Select the CatCalmTV folder from the NEW zip
5. Click OK

### Step 3: Let It Sync (Be Patient!)

**Important:** First sync can take 5-10 minutes depending on internet speed.

You'll see:
```
Downloading https://services.gradle.org/distributions/gradle-8.2-bin.zip
```

This is normal! Gradle 8.2 is ~100MB.

Then:
```
Resolving dependencies...
```

This is also normal! It's downloading libraries.

**DO NOT CLICK ANYTHING** while it's downloading!

### Step 4: If Still Fails - Check These

#### A. Internet/Firewall Issues

**Test your connection:**
```bash
curl https://services.gradle.org/distributions/gradle-8.2-bin.zip -I
```

Should show: `HTTP/2 200`

If it fails:
- Check firewall
- Try disabling VPN
- Try different network
- Check if antivirus is blocking

#### B. Proxy Settings

If you're behind a corporate proxy:

1. File > Settings > Appearance & Behavior > System Settings > HTTP Proxy
2. Enter your proxy details
3. Or set in `gradle.properties`:
   ```properties
   systemProp.http.proxyHost=your.proxy.com
   systemProp.http.proxyPort=8080
   systemProp.https.proxyHost=your.proxy.com
   systemProp.https.proxyPort=8080
   ```

#### C. Force Online Download

In Android Studio Terminal:
```bash
./gradlew --refresh-dependencies
```

#### D. Use Android Studio's Built-in Gradle

1. File > Settings > Build > Build Tools > Gradle
2. Select "Use Gradle from: 'wrapper'"
3. Gradle JDK: Select "jbr-17" (Android Studio's JDK)

### Step 5: Nuclear Option - Offline Build

If you STILL can't download dependencies:

**Option A: Use Android Studio's SDK Manager**
1. Tools > SDK Manager
2. SDK Tools tab
3. Install everything under "SDK Build-Tools"
4. This gets some dependencies locally

**Option B: Manual Dependency Download**

This is complex, but here's how:

1. Download these manually:
   - https://dl.google.com/android/repository/build-tools_r34.0.0-windows.zip
   - https://repo1.maven.org/maven2/androidx/leanback/leanback/1.0.0/leanback-1.0.0.aar
   - https://repo1.maven.org/maven2/androidx/appcompat/appcompat/1.6.1/appcompat-1.6.1.aar

2. Place in local Maven repository structure (advanced)

**This is very complex - only do if nothing else works!**

## Alternative: Pre-built APK

If you absolutely cannot build:

**I can provide you with a pre-built APK** if you just want to test the app quickly. Let me know and I'll create one using GitHub Actions or another CI system.

## Check Your System

### Required:
- ✅ Android Studio Hedgehog or newer
- ✅ JDK 17 (comes with Android Studio)
- ✅ 10GB+ free disk space
- ✅ Stable internet (for first build)

### Verify:
```bash
# Check Java version
java -version
# Should show: version "17" or higher

# Check disk space
df -h  # Mac/Linux
# Windows: just check in File Explorer
```

## Common Error Solutions

### "Unable to find method"
→ You opened the old project. Close it and open the NEW clean version.

### "Dependency cache corrupt"
→ Delete `~/.gradle/caches/` completely and sync again

### "Network timeout"
→ Increase timeout in gradle.properties:
```properties
systemProp.org.gradle.internal.http.connectionTimeout=180000
systemProp.org.gradle.internal.http.socketTimeout=180000
```

### "Plugin with id 'com.android.application' not found"
→ Check your internet. The plugin needs to download.

### "SDK location not found"
→ Android Studio should auto-create local.properties
→ If not: File > Project Structure > SDK Location

## Last Resort: Command Line Build

If Android Studio GUI keeps failing:

```bash
cd CatCalmTV

# Use specific Gradle version
gradle wrapper --gradle-version 8.2 --distribution-type bin

# Clean
./gradlew clean

# Build
./gradlew assembleDebug --stacktrace --info

# If successful, APK is at:
# app/build/outputs/apk/debug/app-debug.apk
```

The `--stacktrace --info` flags show exactly what's failing.

## Still Not Working?

### Tell me:
1. What's the EXACT error message?
2. Your OS (Mac/Windows/Linux)?
3. Android Studio version?
4. Can you access https://services.gradle.org in browser?
5. Are you behind corporate firewall/proxy?

### I can:
- Provide pre-built APK
- Create even simpler build config
- Help with specific error messages
- Create offline bundle with dependencies

## This Clean Version Should Work

The new version has:
- Simpler dependency resolution
- Longer network timeouts
- Latest stable Gradle/Plugin versions
- Minimal dependencies (just 2!)

If this doesn't work, there's likely a system/network issue preventing downloads.

---

**Don't give up!** We'll get this working. 🚀
