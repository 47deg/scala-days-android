import Libraries.android._
import Libraries.macroid._
import Libraries.json._
import Libraries.test._
import Libraries.graphics._
import Libraries.social._
import Libraries.date._
import Libraries.qr._
import Libraries.playServices._
import Generator._

android.Plugin.androidBuild

platformTarget in Android := Versions.androidPlatformV

name := """scala-days-android"""

organization := "com.fortysevendeg"

organizationName := "47 Degrees"

organizationHomepage := Some(new URL("http://47deg.com"))

version := Versions.appV

scalaVersion := Versions.scalaV

scalacOptions ++= Seq("-feature", "-deprecation")

credentials += Credentials(new File(Path.userHome.absolutePath + "/.ivy2/.credentials"))

resolvers ++= Settings.resolvers

libraryDependencies ++= Seq(
  aar(macroidRoot),
  aar(androidAppCompat),
  aar(androidCardView),
  aar(androidRecyclerview),
  aar(macroidExtras),
  aar(playServicesBase),
  aar(playServicesMaps),
  playJson,
  specs2,
  mockito,
  androidTest,
  picasso,
  twitter4j,
  prettytime,
  zxingCore,
  aar(zxingAndroid),
  compilerPlugin(Libraries.wartRemover))

run <<= run in Android

proguardScala in Android := true

useProguard in Android := true

proguardOptions in Android ++= Settings.proguardCommons

apkbuildExcludes in Android ++= Seq(
  "META-INF/LICENSE",
  "META-INF/LICENSE.txt",
  "META-INF/NOTICE",
  "META-INF/NOTICE.txt")


manifestPlaceholders in Android := manifestPlaceholderMap
