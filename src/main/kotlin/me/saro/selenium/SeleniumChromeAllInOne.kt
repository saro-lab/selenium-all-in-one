package me.saro.selenium

import me.saro.selenium.comm.Utils
import me.saro.selenium.model.DownloadStrategy
import me.saro.selenium.model.PathManager
import me.saro.selenium.model.Platform
import me.saro.selenium.service.ChromeBinManager
import me.saro.selenium.service.WebDriverPlus
import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File

class SeleniumChromeAllInOne private constructor(
    private val chromeBinPath: String,
    private val options: Set<String>
) {
    fun <T> openBackground(url: String, use: WebDriverPlus.() -> T): T =
        openWith(url, setOf("--headless"), use)

    fun <T> openBackground(url: String, addOption: Set<String>, use: WebDriverPlus.() -> T): T =
        openWith(url, addOption + setOf("--headless"), use)

    fun <T> openWith(url: String, use: WebDriverPlus.() -> T): T =
        openWith(url, setOf(), use)

    fun <T> openWith(url: String, addOption: Set<String>, use: WebDriverPlus.() -> T): T {
        val driver = createCustom(createChromeOptions(addOption))
        try {
            return use(WebDriverPlus(driver).apply {
                driver.manage().window().size = Dimension(2000, 3000)
                move(url)
            })
        } finally {
            try { driver.close() } catch (e: Exception) {}
            try { driver.quit() } catch (e: Exception) {}
            log.info("chrome driver closed.")
        }
    }

    fun createCustom(chromeOptions: ChromeOptions): ChromeDriver =
        ChromeDriver(chromeOptions.setBinary(chromeBinPath))

    private fun createChromeOptions(addOption: Set<String>) =
        ChromeOptions().apply {
            options.forEach(::addArguments)
            addOption.forEach(::addArguments)
        }

    companion object {
        private val log = Utils.getLogger(SeleniumChromeAllInOne::class)

        private var created = false

        @JvmStatic
        fun builder(manageChromePath: File): Builder = Builder(File(manageChromePath.canonicalPath))

        @JvmStatic
        fun download(manageChromePath: File, platform: Platform, downloadStrategy: DownloadStrategy) {
            if (downloadStrategy == DownloadStrategy.THROW_IF_NO_VERSION) {
                throw RuntimeException("your ChromeDownloadOption is JUST_MAJOR_VERSION_CHECK_OR_THROW, change your ChromeDownloadOption")
            }
            ChromeBinManager.load(PathManager.create(manageChromePath, platform), downloadStrategy)
        }
    }

    class Builder(
        private val manageChromePath: File,
    ) {
        private var downloadStrategy: DownloadStrategy = DownloadStrategy.DOWNLOAD_IF_NO_VERSION
        private val chromeOptions: MutableSet<String> = mutableSetOf()
        private val systemProperties: MutableMap<String, String> = mutableMapOf()
        private val log = Utils.getLogger(Builder::class)

        fun chromeDownloadOption(downloadStrategy: DownloadStrategy): Builder {
            this.downloadStrategy = downloadStrategy
            return this
        }

        fun chromeOption(option: String): Builder {
            assert (option.isNotBlank()) { "option is blank" }
            val lof = option.lastIndexOf('=')
            if (lof != -1) {
                val key = option.substring(0, lof)
                chromeOptions.removeIf { it.startsWith(key) }
            }
            when (option) {
                "--headless" -> log.warning("It is not recommended for users to change the value of webdriver.chrome.driver.")
            }
            chromeOptions.add(option)
            return this
        }

        fun enableRecommendChromeOptions(disabledSecurity: Boolean): Builder {
            chromeOption("--user-data-dir=" + System.getProperty("java.io.tmpdir")) // Prevents socket errors.
                .chromeOption("--disable-infobars") // Disables browser information bar.
                .chromeOption("--disable-dev-shm-usage") // Ignores the limit on temporary disk space for the browser.
                .chromeOption("--blink-settings=imagesEnabled=false") // Disables image loading.
                .chromeOption("--disable-extensions")
                .chromeOption("--disable-popup-blocking")
                .chromeOption("--disable-gpu")
            if (disabledSecurity) {
                systemProperties["webdriver.chrome.whitelistedIps"] = ""
                chromeOption("--no-sandbox")
                    .chromeOption("--ignore-certificate-errors")
            }
            return this
        }

        private fun print() {
            log.info("Selenium Chrome All-in-One System Properties")
            systemProperties.forEach { (k, v) -> log.info("$k : $v") }
            log.info("Selenium Chrome All-in-One Chrome Options")
            chromeOptions.forEach { log.info(it) }
        }

        @Synchronized
        fun build(): SeleniumChromeAllInOne {
            if (created) {
                log.warning("SeleniumAllInOne is already created.\nIt is a singleton object.")
            }
            val pathManager = PathManager.create(manageChromePath)
            ChromeBinManager.load(pathManager, downloadStrategy)
            systemProperties.forEach(System::setProperty)
            System.setProperty("webdriver.chrome.driver", pathManager.chromedriverBinPath)
            created = true
            return SeleniumChromeAllInOne(pathManager.chromeBinPath, chromeOptions.toSet())
        }
    }
}
