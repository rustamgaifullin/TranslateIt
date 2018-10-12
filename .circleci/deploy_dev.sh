curl \
-F "status=2" \
-F "notify=2" \
-F "ipa=@app/build/outputs/apk/debug/app-debug.apk" \
-H "X-HockeyAppToken: $HockeyAppToken" \
https://rink.hockeyapp.net/api/2/apps/$AndroidAppId_Dev/app_versions/upload