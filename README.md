# airship-location-cordova (Deprecated)

A Cordova plugin that automatically links the Airship Location modules in iOS and Android.

### Resources:
 - [Cordova getting started guide](http://docs.urbanairship.com/platform/cordova/)
 - [JSDocs](http://docs.urbanairship.com/reference/libraries/airship-location-cordova/latest/)

## Installation

```xml
	cordova plugin add https://github.com/urbanairship/airship-location-cordova
```

### Contributing Code

We accept pull requests! If you would like to submit a pull request, please fill out and submit our
[Contributor License Agreement](https://docs.google.com/forms/d/e/1FAIpQLScErfiz-fXSPpVZ9r8Di2Tr2xDFxt5MgzUel0__9vqUgvko7Q/viewform).

One of our engineers will verify receipt of the agreement before approving your pull request.

### Issues

Please visit http://support.urbanairship.com/ for any issues integrating or using this plugin.

### Requirements:
 - cordova >= 9.0.1
 - cordova-ios >= 6.1.0
 - cordova-android >= 9.0.0
 - cococapods >= 1.7.3
 - urbanairship-cordova plugin  >= 8.0.0


 ### Quickstart

1. Install this plugin using Cordova CLI:

        cordova plugin add https://github.com/urbanairship/airship-location-cordova

2. (iOS only) Add required location authorization strings to your iOS project's info.plist:

		<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
		<string>Always and when in use location usage description</string>
		<key>NSLocationAlwaysUsageDescription</key>
		<string>Always location usage description</string>
		<key>NSLocationWhenInUseUsageDescription</key>
		<string>When in use location usage description</string>

3. Enable location:

        // Enable location (will prompt the user to accept location on iOS)
        AirshipLocation.setLocationEnabled(true, function (enabled) {
            console.log("Location is enabled!")
        })
