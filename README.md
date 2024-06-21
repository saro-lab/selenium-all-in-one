# Selenium All-in-One
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/selenium-chrome-all-in-one/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/selenium-chrome-all-in-one)
[![GitHub license](https://img.shields.io/github/license/saro-lab/selenium-chrome-all-in-one.svg)](https://github.com/saro-lab/selenium-chrome-all-in-one/blob/master/LICENSE)

# Introduction

Selenium Chrome All-in-One is a library that automatically downloads Chrome/ChromeDriver and helps you use Selenium with those files.

Try the example below.

# QUICK START

## Gradle

```
compile 'me.saro:selenium-chrome-all-in-one:4.21.0.1'
```

## Maven

``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>selenium-chrome-all-in-one</artifactId>
  <version>4.21.0.1</version>
</dependency>
```

## Kotlin example
```kotlin
// use example
val chromeBinPath = File("./chrome-bin")

val manager = ChromeDriverManager.builder(chromeBinPath)
    //.downloadStrategy(DOWNLOAD_IF_NO_VERSION) // default value
    .enableRecommendChromeOptions(true)
    .build()

val list = manager.openBackground("https://anissia.net") {
    finds(".flex.items-center.py-3.my-1.border-b.border-gray-200.text-sm.anissia-home-reduce-10")
        .map { it.find("a").text }
}

list.forEach(::println)
```
```kotlin
// just download
val chromeBinPath = File("./chrome-bin")
ChromeDriverManager.download(chromeBinPath, getPlatform(), DOWNLOAD_IF_NO_VERSION)
```
```kotlin
// with spring project
@Configuration
@EnableAutoConfiguration
class SeleniumConfiguration {
    @Bean
    fun getChromeDriverManager(): ChromeDriverManager =
        ChromeDriverManager
            .builder(File("./chrome-bin"))
            .enableRecommendChromeOptions(true)
            .build()
}
// use example
@Service
class ScrapTradeService(
    private val chromeDriverManager: ChromeDriverManager,
    private val tradingVolumeRepository: TradingVolumeRepository,
): ScrapTrade {
    ...
}
```

## Java example
```java
// use example
String chromeBinPath = new File("./chrome-bin");

ChromeDriverManager manager = ChromeDriverManager.builder(chromeBinPath)
        .enableRecommendChromeOptions(true)
        .build();

List<String> list = manager.openBackground("https://anissia.net", dp -> {
    List<String> items = new ArrayList<>();
    dp.finds(".flex.items-center.py-3.my-1.border-b.border-gray-200.text-sm.anissia-home-reduce-10").forEach(
            e -> items.add(dp.find(e, "a").getText())
    );
    return items;
});

list.forEach(System.out::println);
```
```java
// just download
String chromeBinPath = new File("./chrome-bin");
SeleniumChromeAllInOne.download(chromeBinPath, Platform.getPlatform(), DownloadStrategy.DOWNLOAD_IF_NO_VERSION);
```

# Documentation

## class ChromeDriverManager
- This is a manager that creates `ChromeDriverPlus` and `ChromeDriver` objects, and it is a singleton class.
- It is recommended to use it by creating it as a `@Bean`.
<details>
<summary style="font-size: 14px; font-weight: bold">static fun builder(manageChromePath: File): ChromeDriverBuilder</summary>

- `manageChromePath`
    - Specify the folder to store and manage the versions of the Chrome browser and ChromeDriver.
    - To avoid conflicts, it is recommended to use a folder created exclusively for the Selenium Chrome All-In-One project.
</details>

<details>
<summary style="font-size: 14px; font-weight: bold">static fun download(manageChromePath: File, platform: Platform, downloadStrategy: DownloadStrategy)</summary>

- `manageChromePath`
    - Specify the folder to store and manage the versions of the Chrome browser and ChromeDriver.
    - To avoid conflicts, it is recommended to use a folder created exclusively for the Selenium Chrome All-In-One project.
- `platform`
    - Specify the platform to download the `Chrome browser` and `Chrome Driver`.
    - Using Platform.getPlatform() allows you to retrieve the current platform you are using.
- `downloadStrategy`
    - `DownloadStrategy.THROW_IF_NO_VERSION`
        - Throws an error if the version does not exist.
    - `DownloadStrategy.DOWNLOAD_IF_NO_VERSION`
        - Downloads if the version does not exist.
    - `DownloadStrategy.DOWNLOAD_IF_NO_VERSION_OR_DIFFERENT_REVISION`
        - Downloads if the version does not exist or if the revision is different.
</details>


<details open>
<summary style="font-size: 14px; font-weight: bold">fun <T> openBackground(url: String, use: ChromeDriverPlus.() -> T): T </summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun <T> openBackground(url: String, addOption: Set<String>, use: ChromeDriverPlus.() -> T): T </summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun <T> openWith(url: String, use: ChromeDriverPlus.() -> T): T </summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun <T> openWith(url: String, addOption: Set<String>, use: ChromeDriverPlus.() -> T): T </summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun newChromeDriver(chromeOptions: ChromeOptions): ChromeDriver </summary>

temp
</details>

## class ChromeDriverPlus
<details open>
<summary style="font-size: 14px; font-weight: bold">var implicitWaitTimeout: Duration</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">var pageLoadTimeout: Duration</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun <T> use(url: String, use: ChromeDriverPlus.() -> T): T</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun move(url: String)</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun scrollDown(size: Int)</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun scrollDown()</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun script(script: String): Any?</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun sleep(millis: Long)</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun windowSize(width: Int, height: Int)</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun find(css: String): WebElement = driver.findElement(By.cssSelector(css))</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun finds(css: String): List<WebElement> = driver.findElements(By.cssSelector(css))</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun findsNotWait(css: String): List<WebElement> = driver.findsNotWait(css)</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun hasElementsNotWait(css: String): Boolean = driver.hasElementsNotWait(css)</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun <T> inImplicitWaitTimeout(duration: Duration, run: () -> T): T</summary>

temp
</details>

<details open>
<summary style="font-size: 14px; font-weight: bold">fun <T> inPageLoadTimeout(duration: Duration, run: () -> T): T</summary>

temp
</details>


## class ChromeDriverBuilder
- Please create this object through `ChromeDriverManager.builder()`.
<details>
<summary style="font-size: 14px; font-weight: bold">fun downloadStrategy(downloadStrategy: DownloadStrategy): ChromeDriverBuilder</summary>

- `DownloadStrategy.THROW_IF_NO_VERSION`
    - Throws an error if the version does not exist.
- `DownloadStrategy.DOWNLOAD_IF_NO_VERSION`
    - Downloads if the version does not exist.
- `DownloadStrategy.DOWNLOAD_IF_NO_VERSION_OR_DIFFERENT_REVISION`
    - Downloads if the version does not exist or if the revision is different.

</details>

<details>
<summary style="font-size: 14px; font-weight: bold">fun option(option: String): ChromeDriverBuilder</summary>

- Enter the options for `ChromeDriver`.
- However, the `--headless` option cannot be used.
- Instead, use `ChromeDriverManager.openBackground()`.

</details>

<details>
<summary style="font-size: 14px; font-weight: bold">fun enableRecommendChromeOptions(disabledSecurity: Boolean): ChromeDriverBuilder</summary>

- recommend chrome options
    ```
    // Prevents socket errors.
    --user-data-dir=System.getProperty("java.io.tmpdir")
    
    // Disables browser information bar.
    --disable-infobars
    
    // Ignores the limit on temporary disk space for the browser.
    --disable-dev-shm-usage
    
    // Disables image loading.
    --blink-settings=imagesEnabled=false
    
    --disable-extensions
    --disable-popup-blocking
    --disable-gpu
    ```
- disabled security options
    ```
    webdriver.chrome.whitelistedIps = "" (system properties)
    --no-sandbox
    --ignore-certificate-errors
    ```
</details>

<details>
<summary style="font-size: 14px; font-weight: bold">fun build(): ChromeDriverManager</summary>

- create `ChromeDriverManager` object
</details>

# Version info
- CDP: Chrome DevTools Protocol (Version) == Chrome Browser Version

| Selenium All-in-One / CDP | Selenium | Selenium Exact CDP | Selenium Support CDP |
|---------------------------|----------|--------------------|----------------------|
| 4.21.0.2 / 123            | 4.21.0   | 123                | 123 ~ 125            |
| 4.21.0.1 / 125            | 4.21.0   | 123                | 123 ~ 125            |

## Repository
- https://search.maven.org/artifact/me.saro/selenium-chrome-all-in-one
- https://mvnrepository.com/artifact/me.saro/selenium-chrome-all-in-one

## Selenium Project
- https://www.selenium.dev/
