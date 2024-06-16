package me.saro.selenium.comm

import me.saro.selenium.SeleniumChromeAllInOne.Companion.download
import java.io.File
import java.util.*

class ChromeLoader(
    private val path: File,
    private val downloadOption: ChromeDownloadOption,
    private val props: Properties = Properties().apply { {}.javaClass.getResourceAsStream("/application.properties").use { load(it) } },
    private val chromeDownloadUri: String = props.getProperty("chrome.download.uri")!!,
    private val chromeVersion: String = props.getProperty("chrome.version")!!,
    private val chromeVersionDirPrefix: String = "chrome-$chromeVersion-",
) {
    fun load(platform: Platform) {
        val path = chromeVersionPath
        val dirNotExist = path.endsWith('-')
        if ()
    }

    fun checkBinary() {
        val path = chromeVersionPath
        val dirNotExist = path.endsWith('-')
        if (dirNotExist) {
            when (downloadOption) {
                ChromeDownloadOption.JUST_MAJOR_VERSION_CHECK_OR_THROW -> throw SeleniumChromeAllInOneException("chrome binary not found")
                ChromeDownloadOption.IF_MAJOR_VERSIONS_DIFFER_DOWNLOAD -> download()
                ChromeDownloadOption.IF_MINOR_VERSIONS_DIFFER_DOWNLOAD -> download()
            }
        }
    }

    private val chromeVersionPath: String get() {
        if (path.exists()) {
            if (!path.isDirectory) {
                throw RuntimeException("driver path [${path.absolutePath}] is not directory")
            }
        } else {
            path.mkdirs()
        }
        val chromeVersionDirPrefixRegex = Regex("^$chromeVersionDirPrefix\\d+$")
        return path
            .listFiles{ file -> file.isDirectory && file.name.matches(chromeVersionDirPrefixRegex) }
            ?.reduce { a, b ->
                val aVer = a.name.replace(chromeVersionDirPrefix, "").toInt()
                val bVer = b.name.replace(chromeVersionDirPrefix, "").toInt()
                if (aVer > bVer) a else b
            }?.absolutePath ?: chromeVersionDirPrefix
    }
}