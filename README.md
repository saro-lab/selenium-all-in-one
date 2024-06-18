# Selenium All-in-One
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/selenium-chrome-all-in-one/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/selenium-chrome-all-in-one)
[![GitHub license](https://img.shields.io/github/license/saro-lab/selenium-chrome-all-in-one.svg)](https://github.com/saro-lab/selenium-chrome-all-in-one/blob/master/LICENSE)

# Introduction

Selenium Chrome All-in-One is a library that automatically downloads Chrome/ChromeDriver and helps you use Selenium with those files.

Try the example below.

# QUICK START

## Gradle

```
compile 'me.saro:selenium-chrome-all-in-one:4.21.0.0'
```

## Maven

``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>selenium-chrome-all-in-one</artifactId>
  <version>4.21.0.0</version>
</dependency>
```

## Kotlin example
```kotlin
// use example
val chromeBinPath = "./chrome-bin"

val sca = SeleniumChromeAllInOne.builder(File(chromeBinPath))
    // .downloadStrategy(DOWNLOAD_IF_NO_VERSION) default value
    .enableRecommendChromeOptions(true)
    .build()

val list = sca.openBackground("https://anissia.net") {
    driver.finds(".flex.items-center.py-3.my-1.border-b.border-gray-200.text-sm.anissia-home-reduce-10")
        .map { it.find("a").text }
}
list.forEach(::println)
```
```kotlin
// just download
val chromeBinPath = "./chrome-bin"
download(File(chromeBinPath), getPlatform(), DOWNLOAD_IF_NO_VERSION)
```
```kotlin
// with spring project
@EnableAutoConfiguration
@Configuration
class SeleniumConfiguration {
    @Bean
    fun getSelenium() =
        SeleniumChromeAllInOne.builder(File("./chrome-bin"))
            .enableRecommendChromeOptions(true)
            .build()
}
```

## Java example
```java
// use example
String chromeBinPath = "./chrome-bin";

var sca = SeleniumChromeAllInOne.builder(new File(chromeBinPath))
        .enableRecommendChromeOptions(true)
        .build();

var list = sca.openBackground("https://anissia.net", dp -> {
    var driver = dp.getDriver();
    var items = new ArrayList<>();

    dp.finds(driver, ".flex.items-center.py-3.my-1.border-b.border-gray-200.text-sm.anissia-home-reduce-10").forEach(
            e -> items.add(dp.find(e, "a").getText())
    );

    return items;
});

list.forEach(System.out::println);
```
```java
// just download
val chromeBinPath = "./chrome-bin"
SeleniumChromeAllInOne.download(new File(chromeBinPath), Platform.getPlatform(), DownloadStrategy.DOWNLOAD_IF_NO_VERSION);
```

## Repository
- https://search.maven.org/artifact/me.saro/selenium-chrome-all-in-one
- https://mvnrepository.com/artifact/me.saro/selenium-chrome-all-in-one

## Selenium Project
- https://www.selenium.dev/
