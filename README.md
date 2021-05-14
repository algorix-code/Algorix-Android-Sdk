Import SDK
dependent jar packages Copy the alx....aar in the SDKcompressed package to the Application Module/libs folder (if not, you must create it manually), and add the following code to the build.gradle of your Moudle app: //The project Application name exported by Unity is generally unityLibrary 
repositories {
     flatDir {
         dirs'libs'
     }
}
depedencies {
     compile(name:'alx.*.*.*', ext:'aar') //Change to the specific version number by yourself

}
Permissions
Alx SDK recommends that you add the following permissions, and it is recommended to declare to the developer in your privacy agreement that Alx SDK will obtain the following permissions and apply them to advertising.
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

Adapt to Android7.0
Adapt to Android7.0 and above If your application needs to run on Android7.0 and above, please add the following code in AndroidManifest.

<provider
     android:name="com.alxad.provider.AlxFileProvider"
     android:authorities="${applicationId}.alxfileprovider"
     android:exported="false"
     android:grantUriPermissions="true">
     <meta-data
         android:name="android.support.FILE_PROVIDER_PATHS"
         android:resource="@xml/alx_file_path" />
</provider>

In the res/xml directory, create a new xml file alx_file_path.xml, and add the following code to the file:

<?xml version="1.0" encoding="utf-8"?>
<resources>
     <paths>
         <external-path
             name="external_files"
             path="." />
     </paths>
</resources>

Environment

Operating environment configuration This SDK can run on Android4.0 (API Level 14) and above. If the developer declares that the targetSdkVersion is above API 23, please ensure that you have applied for all the permissions required by the SDK before calling any interface of this SDK, otherwise some features of the SDK may be restricted.

Code obfuscation

Code obfuscation settings Add the following to the .pro file in the App folder (usually called proguard-rules.pro in Android, and proguard-unity.txt exported by Unity):
-keep class com.alxad.** {*;}

Support

Alx sdk supports banner and Reward Viedo Adapt to Admob Mopub Topon. More information see the code in the demo


