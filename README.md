# react-native-baidu-location

[![npm package](https://img.shields.io/npm/v/react-native-baidu-geolocation.svg?style=flat-square)](https://www.npmjs.org/package/react-native-baidu-geolocation)
[![npm downloads](https://img.shields.io/npm/dt/react-native-baidu-geolocation.svg)](https://www.npmjs.com/package/react-native-baidu-geolocation)

<img width="300" src="https://github.com/xinlc/react-native-baidu-location/blob/master/doc/ios.jpg?raw=true" />

## TOC

* [Installation](#installation)
* [Linking](#linking)
* [Usage](#usage)

## Installation

Using npm:

```shell
npm install --save react-native-baidu-geolocation
```

or using yarn:

```shell
yarn add react-native-baidu-geolocation
```

## Linking

### Automatic

```shell
react-native link react-native-baidu-geolocation
```

### Manual

<details>
    <summary>Android</summary>

* In `android/app/build.gradle`:

```diff
dependencies {
    ...
    compile "com.facebook.react:react-native:+"  // From node_modules
+   compile project(':react-native-baidu-geolocation')
    ...
}
```

* In `android/settings.gradle`:

```diff
...
include ':app'
+ include ':react-native-baidu-geolocation'
+ project(':react-native-baidu-geolocation').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-baidu-geolocation/android')
...
```

#### With React Native 0.29+

* In `MainApplication.java`:

```diff
...
+ import com.rnbaidulocation.RNBaidulocPackage;

  public class MainApplication extends Application implements ReactApplication {
    ...

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
+         new RNBaidulocPackage()
      );
    }

    ...
  }
```

#### With older versions of React Native:

* In `MainActivity.java`:

```diff
...
+ import com.rnbaidulocation.RNBaidulocPackage;

  public class MainActivity extends ReactActivity {
    ...

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
+       new RNBaidulocPackage()
      );
    }
  }
```

* In `AndroidManifest.xml`
```diff
+        <meta-data
+                android:name="com.baidu.lbsapi.API_KEY"
+                android:value="Your AK" />
+        <service android:name="com.baidu.location.f" android:enabled="true" android:process=":remote"></service>
```
</details>

<details>
    <summary>iOS</summary>

In XCode, in the project navigator:

* Right click _Libraries_
* Add Files to _[your project's name]_
* Go to `node_modules/react-native-baidu-geolocation/ios`
* Add the `RNBaiduLocation.xcodeproj` file

Click on project _General_ tab

* Under _Linked Frameworks and Libraries_ click `+` and add `libstdc++.6.0.9.tbd`, `libsqlite3.0.tbd`, `RNBaiduLocation.framework`, `BaiduMapAPI_Search.framework`, `BaiduMapAPI_Base.framework`, `BaiduMapAPI_Location.framework`

Click on project _Build Settings_ tab

* Look for _Framework Search Paths_ and make sure it contain `$(SRCROOT)/../node_modules/react-native-baidu-geolocation/ios/RNBaiduLocation/vender`
* Look for _Header Search Paths_ and make sure it contain `$(SRCROOT)/../node_modules/react-native-baidu-location/ios/RNBaiduLocation` (Mark as recursive)

In the project navigator:

* Click _Info.plist_
* Add the `NSLocationWhenInUseUsageDescription` to your `Info.plist` with strings describing why your app needs these permissions.

In _AppDelegate.m_
```diff
+  _mapManager = [[BMKMapManager alloc] init];
+  BOOL ret = [_mapManager start:@"Your AK" generalDelegate:nil];
+  if (!ret) {
+    NSLog(@"baidu map error.");
+  }
```

Run your project (Cmd+R) 

</details>

## Usage
See the [example](https://github.com/xinlc/react-native-baidu-location/blob/master/examples/BaiduLocationDemo/App.js)

```js
...
import NRBaiduloc from 'react-native-baidu-geolocation';

NRBaiduloc.start();
...
```
