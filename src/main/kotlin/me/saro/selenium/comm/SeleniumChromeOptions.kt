package me.saro.selenium.comm

import java.io.File
import java.util.*

class SeleniumChromeOptions(
    private val path: File,
    private val chromeOptions: Set<String>,
) {
    val log = Utils.getLogger(SeleniumChromeOptions::class)
    val seleniumVersion: String
    val chromeVersion: String
    val chromeDownloadUri: String


    val chromePath: String get() {
        return ""
    }

    init {
        Properties().apply { {}.javaClass.getResourceAsStream("/application.properties").use { load(it) } }
            .also {
                seleniumVersion = it.getProperty("selenium.version")!!
                chromeVersion = it.getProperty("chrome.version")!!
                chromeDownloadUri = it.getProperty("chrome.download.uri")!!
            }
    }
}