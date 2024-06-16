package me.saro.selenium.comm

import java.io.File
import java.util.*

class SeleniumAioOptions(
    private val path: File,
    private val chromeDownloadOption: ChromeDownloadOption = ChromeDownloadOption.IF_MAJOR_VERSIONS_DIFFER_DOWNLOAD,
    private val chromeOptions: Set<String>,
    private val systemProperties: Map<String, String>
) {
    val log = Utils.getLogger(SeleniumAioOptions::class)
    val seleniumVersion: String
    val chromeVersion: String
    val chromeDownloadUri: String
    val chromeVersionDirPrefix: String get() = "chrome-$chromeVersion"

    val chromePath: String get() {
        if (path.exists()) {
            if (!path.isDirectory) {
                throw RuntimeException("driver path [${path.canonicalPath}] is not directory")
            }
        } else {
            path.mkdirs()
        }
        return path
            .listFiles{ file -> file.isDirectory && file.name.matches(Regex("^$chromeVersionDirPrefix\\d+$")) }
            ?.reduce { a, b ->
                val aVer = a.name.replace(chromeVersionDirPrefix, "").toInt()
                val bVer = b.name.replace(chromeVersionDirPrefix, "").toInt()
                if (aVer > bVer) a else b
            }?.canonicalPath ?: chromeVersionDirPrefix
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