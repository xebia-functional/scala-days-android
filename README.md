![Build Status](https://travis-ci.org/47deg/scala-days-android.svg?branch=master)

Scala Days for Android
============================

The official Scala Days App for Android handcrafted by [47 Degrees](http://www.47deg.com). You can download Scala Days from [Google Play](https://play.google.com/store/apps/details?id=com.fortysevendeg.android.scaladays)

Scala on Android
==============

This application is written entirely in Scala on Android. We are excited to make the application open source and share the code with you. We have used the [macroid](http://macroid.github.io/) library extensively in this project.

Compile
======

You can compile this project and contribute improvements. To compile the project:

* Download [SBT](http://www.scala-sbt.org/download.html) and install it
* Download [Android SDK](https://developer.android.com/studio/index.html#downloads) (you only need the command line tools) and set `ANDROID_HOME` environment variable pointing to the root folder.
* Clone this GitHub project to your computer
* From project root directory run:

```
$ sbt -mem 2048
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
crashlytics.apikey=***
crashlytics.apisecret=***
```

If you do not want to utilize the `Crashlytics` service, you should create an environment variable `CRASHLYTICS_ENABLED=false`


Contribute
========

You can contribute to this application and we will do our best to upload your changes to Google Play

License
======

Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
