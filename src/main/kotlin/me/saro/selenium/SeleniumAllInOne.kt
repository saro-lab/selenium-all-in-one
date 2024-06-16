package me.saro.selenium

import me.saro.selenium.comm.ChromeDownloadOption
import me.saro.selenium.comm.SeleniumAioOptions
import me.saro.selenium.comm.Utils
import java.io.File

class SeleniumAllInOne private constructor(
    private val options: SeleniumAioOptions
) {
    companion object {
        private var created = false

        @JvmStatic
        fun builder(path: File): Builder = Builder(path)
    }

    class Builder(
        private val path: File,
    ) {
        private var chromeDownloadOption: ChromeDownloadOption = ChromeDownloadOption.IF_MAJOR_VERSIONS_DIFFER_DOWNLOAD
        private val chromeOptions: MutableSet<String> = mutableSetOf()
        private val systemProperties: MutableMap<String, String> = mutableMapOf()
        private val log = Utils.getLogger(Builder::class)

        fun chromeDownloadOption(chromeDownloadOption: ChromeDownloadOption): Builder {
            this.chromeDownloadOption = chromeDownloadOption
            return this
        }

        fun systemProperty(key: String, value: String?): Builder {
            assert (key.isNotBlank()) { "key is blank" }
            when (key) {
                "webdriver.chrome.driver" ->
                    throw IllegalArgumentException("$key is not allowed to change the value.")
            }
            if (value != null) {
                systemProperties[key] = value
            } else {
                systemProperties.remove(key)
            }
            return this
        }

        fun chromeOption(option: String): Builder {
            assert (option.isNotBlank()) { "option is blank" }
            removeChromeOptionTree(option)
            when (option) {
                "--headless" -> log.warning("It is not recommended for users to change the value of webdriver.chrome.driver.")
            }
            chromeOptions.add(option)
            return this
        }

        fun enableRecommendChromeOptions(): Builder =
            chromeOption("--user-data-dir=" + System.getProperty("java.io.tmpdir")) // Prevents socket errors.
                .chromeOption("--disable-infobars") // Disables browser information bar.
                .chromeOption("--disable-dev-shm-usage") // Ignores the limit on temporary disk space for the browser.
                .chromeOption("--blink-settings=imagesEnabled=false") // Disables image loading.
                .chromeOption("--disable-extensions")
                .chromeOption("--disable-popup-blocking")
                .chromeOption("--disable-gpu")


        fun disabledSecurityOptions(): Builder =
            systemProperty("webdriver.chrome.whitelistedIps", "")
                .chromeOption("--no-sandbox")
                .chromeOption("--ignore-certificate-errors")

        fun removeChromeOption(option: String): Builder {
            assert (option.isNotBlank()) { "option is blank" }
            removeChromeOptionTree(option)
            chromeOptions.remove(option)
            return this
        }

        private fun removeChromeOptionTree(option: String) {
            val lof = option.lastIndexOf('=')
            if (lof != -1) {
                val key = option.substring(0, lof)
                chromeOptions.removeIf { it.startsWith(key) }
            }
        }

        private fun print() {
            log.info("Selenium Chrome All-in-One System Properties")
            systemProperties.forEach { (k, v) -> log.info("$k : $v") }
            log.info("Selenium Chrome All-in-One Chrome Options")
            chromeOptions.forEach { log.info(it) }
        }

        @Synchronized
        fun build(): SeleniumAllInOne {
            if (created) {
                throw RuntimeException("SeleniumAllInOne is already created.\nIt is a singleton object.")
            }
            created = true
            return SeleniumAllInOne(SeleniumAioOptions(path, chromeDownloadOption, chromeOptions.toSet(), systemProperties.toMap()))
        }
    }
}
