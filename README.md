<h2>Import SDK</h2>
dependent jar packages Copy the alx....aar in the SDKcompressed package to the Application Module/libs folder (if not, you must create it manually), and add the following code to the build.gradle of your Moudle app: //The project Application name exported by Unity is generally unityLibrary 

     repositories {     
          flatDir {
               dirs'libs'
          }      
     }


     depedencies {
        implementation (name:'alx.*.*.*', ext:'aar') //Change to the specific version number by yourself

     }

<h2>Permissions</h2>

Alx SDK recommends that you add the following permissions, and it is recommended to declare to the developer in your privacy agreement that Alx SDK will obtain the following permissions and apply them to advertising.

     
    <!--必要权限-->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <!—可选权限-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <!--可选权限：安卓11及以上，按照实际情况可选择添加此权限-->
    <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"/>
    <!-- 播放器应用需要防止屏幕变暗 -->
    <uses-permission android:name="android.permission.WAKE_LOCK" />


<h2>Environment</h2>

Operating environment configuration This SDK can run on Android4.1 (API Level 16) and above. If the developer declares that the targetSdkVersion is above API 23, please ensure that you have applied for all the permissions required by the SDK before calling any interface of this SDK, otherwise some features of the SDK may be restricted.

<h2>Code obfuscation</h2>

Code obfuscation settings Add the following to the .pro file in the App folder (usually called proguard-rules.pro in Android, and proguard-unity.txt exported by Unity):

     -keep class com.alxad.api.** {*;}

<h2>Support</h2>

Alx sdk supports banner and Reward Viedo Adapt to Admob Mopub Topon. More information see the code in the demo


