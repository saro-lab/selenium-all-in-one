package me.saro.selenium.service

import me.saro.selenium.comm.Utils
import me.saro.selenium.model.ChromeDownloadOption
import me.saro.selenium.model.Platform
import me.saro.selenium.model.SeleniumChromeAllInOneException
import java.io.File
import java.io.IOException
import java.net.URI
import java.util.*

class ChromeManager(
    private val platform: String,
    private val platformRoot: File,
    private val binaryFileExt: String,
    private val downloadOption: ChromeDownloadOption,
) {
    private val log = Utils.getLogger(ChromeManager::class)
    private val chromeDownloadUri: String
    private var chromeVersionPath: String
    private val chromeVersion: String
    private val chromeRevision: String get() = chromeVersionPath.split('-').last()
    val chromedriverBinPath get() = File(platformRoot, "$chromeVersionPath/chromedriver$binaryFileExt")
    val chromeBinPath get() = File(platformRoot, "$chromeVersionPath/chrome$binaryFileExt")
    private val existsBinaries: Boolean get() = (!chromeVersionPath.endsWith('-')&& chromedriverBinPath.exists()&& chromeBinPath.exists())
    val chromeRoot: File get() = File(platformRoot, chromeVersionPath)

    fun handle(): ChromeManager {
        // check major version
        val haveMajorVersionBinaries = this.existsBinaries
        if (haveMajorVersionBinaries) {
            if (downloadOption != ChromeDownloadOption.IF_MINOR_VERSIONS_DIFFER_DOWNLOAD) {
                log.info("chrome $chromeVersion exists and check completed")
                return this
            }
        } else if (downloadOption == ChromeDownloadOption.JUST_MAJOR_VERSION_CHECK_OR_THROW) {
            throw RuntimeException("Chrome binaries not found in $platformRoot, but your ChromeDownloadOption is ${ChromeDownloadOption.JUST_MAJOR_VERSION_CHECK_OR_THROW.name}")
        }
        try {
            // download info
            val milestone = Utils.readJson(URI(chromeDownloadUri)).at("/milestones/$chromeVersion")
            val revision = milestone.path("revision").asText()
            val chromeDriverUri = milestone.at("/downloads/chromedriver").first { it.path("platform").asText() == platform }.path("url").asText()
            val chromeUri = milestone.at("/downloads/chrome").first { it.path("platform").asText() == platform }.path("url").asText()
            // check minor version
            if (haveMajorVersionBinaries && chromeRevision == revision) {
                log.info("chrome $chromeVersion.$chromeRevision exists and check completed")
                return this
            }
            chromeVersionPath = "chrome-$chromeVersion-${revision}"
            // not exists and download
            log.info("chrome $chromeVersion.$chromeRevision not exists and download ready")
            downloadAndUnzip(chromeDriverUri, File(chromeRoot, "#chromedriver.zip"))
            downloadAndUnzip(chromeUri, File(chromeRoot, "#chrome.zip"))
            if (this.existsBinaries) {
                log.info("Chrome $chromeVersion.$chromeRevision ready completed")
            } else {
                throw SeleniumChromeAllInOneException("download and unzip completed, but not found binaries in $chromeRoot")
            }
        } catch (e: Exception) {
            when (e) {
                is IOException, is SeleniumChromeAllInOneException -> throw e
                else -> throw SeleniumChromeAllInOneException("failed to get versions $chromeVersion.$chromeRevision in $chromeDownloadUri", e)
            }
        }
        return this
    }

    private fun downloadAndUnzip(uri: String, file: File) {
        if (!(file.exists() && file.isFile && file.length() > 0)) {
            file.parentFile.mkdirs()
            if (file.exists()) {
                file.delete()
                file.createNewFile()
            }
            log.info("download $file in $uri")
            URI.create(uri).toURL().openStream().use { input -> file.outputStream().use(input::copyTo) }
            log.info("download $file completed")
        }
        try {
            Utils.unzip(file, 1, file.parentFile)
        } catch (e: Exception) {
            try { file.delete() } catch (_: Exception) {}
            throw SeleniumChromeAllInOneException("failed to unzip ${file.absolutePath}", e)
        }
        log.info("download $file unzip completed")
    }

    init {
        val props: Properties = Properties().apply { {}.javaClass.getResourceAsStream("/application.properties").use { load(it) } }
        this.chromeDownloadUri = props.getProperty("chrome.download.uri")!!
        this.chromeVersion = props.getProperty("chrome.version")!!
        val chromeVersionDirPrefix = "chrome-$chromeVersion-"
        val chromeVersionDirPrefixRegex = Regex("^$chromeVersionDirPrefix\\d+$")
        this.chromeVersionPath = platformRoot
            .listFiles{ file -> file.isDirectory && file.name.matches(chromeVersionDirPrefixRegex) }
            ?.reduceOrNull { a, b ->
                val aVer = a.name.replace(chromeVersionDirPrefix, "").toInt()
                val bVer = b.name.replace(chromeVersionDirPrefix, "").toInt()
                if (aVer > bVer) a else b
            }?.name ?: chromeVersionDirPrefix
    }

    companion object {
        fun create(root: File, platform: Platform, downloadOption: ChromeDownloadOption): ChromeManager {
            val platformRoot = File(root.canonicalPath, platform.value)
            if (!platformRoot.exists()) {
                platformRoot.mkdirs()
            } else if (!platformRoot.isDirectory) {
                throw RuntimeException("driver path [${platformRoot.canonicalPath}] is not directory")
            }
            val binaryFileExt = when (platform) {
                Platform.WINDOWS_64 -> ".exe"
                else -> ""
            }
            return ChromeManager(platform.value, platformRoot, binaryFileExt, downloadOption)
        }
    }
}