Scala Days for Android
============================

The official Scala Days App for Android handcrafted by [47 Degrees](http://www.47deg.com). You can download Scala Days from [Google Play](https://play.google.com/store/apps/details?id=com.fortysevendeg.android.scaladays)

Scala on Android
==============

This application is written entirely in Scala on Android. We are excited to make the application open source and share the code with you. We have used the [macroid](http://macroid.github.io/) library extensively in this project. In addition we have contributed our own Macroid extensions to this application, that can be found here: [macroid-extras](https://github.com/47deg/macroid-extras).

Compile
======

You can compile this project and contribute improvements. To compile the project:

* Download [Activator](https://typesafe.com/community/core-tools/activator-and-sbt) and install it
* Configure the Android SDK on your computer
* Clone this GitHub project to your computer
* From project root directory run:

```
$ ./activator
```

* Connect your phone and execute:

```
> run
```

You can use your favorite IDE. At 47 Degrees we use IntelliJ with the Scala plugin. If you want to run this project from IntelliJ you only need to import the project.

Add Debug Keys
========

You need to add a `debug.properties` file to the root project with the necessary keys to compile. The content should be:

```
google.map.key=***
localytics.key=***
google.project.number=***
google.analytics.key=***
twitter.app.key=***
twitter.app.secret=***
twitter.app.callback.host=***
crashlytics.key=***
```

add a `crashlytics.properties` file with the following secret keys:

```
apiKey=***
apiSecret=***
``` 

If you do not want to utilize the `Crashlytics` service, you should remove the `Crashlytics.start(this)` line in `MainActivity.scala`


Contribute
========

You can contribute to this application and we will do our best to upload your changes to Google Play

License
======

Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
