package me.saro.selenium.comm

import java.io.File
import java.net.URI
import java.util.*

class ChromeLoader(
    private val platform: String,
    private val platformRoot: File,
    private val binaryFileExt: String,
    private val downloadOption: ChromeDownloadOption,
) {
    private val log = Utils.getLogger(ChromeLoader::class)
    private val chromeDownloadUri: String
    private var chromeVersionPath: String
    private val chromeVersion: String
    private val chromeRevision: String get() = chromeVersionPath.split('-').last()
    private val existsBinaries: Boolean get() = (!chromeVersionPath.endsWith('-')&&
            File(platformRoot, "$chromeVersionPath/chromedriver$binaryFileExt").exists()&&
            File(platformRoot, "$chromeVersionPath/chrome$binaryFileExt").exists())
    val chromeRoot: File get() = File(platformRoot, chromeVersionPath)

    fun load() {
        val existsBinaries = this.existsBinaries
        if (existsBinaries) {
            if (downloadOption != ChromeDownloadOption.IF_MINOR_VERSIONS_DIFFER_DOWNLOAD) {
                log.info("Chrome $chromeVersion load completed")
                return
            }
        } else if (downloadOption == ChromeDownloadOption.JUST_MAJOR_VERSION_CHECK_OR_THROW) {
            throw RuntimeException("Chrome binaries not found in $platformRoot, but your ChromeDownloadOption is ${ChromeDownloadOption.JUST_MAJOR_VERSION_CHECK_OR_THROW.name}")
        }
        download(existsBinaries)
    }

    private fun download(existsBinaries: Boolean) {
        val map = MapHelper.byURI(URI(chromeDownloadUri))
        try {
            val milestone = map.getMap("milestones")!!.getMap(chromeVersion)!!
            val revision = milestone.getString("revision", "")
            if (existsBinaries && this.chromeRevision == revision) {
                log.info("Chrome $chromeVersion / $revision load completed")
                return
            }
            chromeVersionPath = "chrome-$chromeVersion-$revision"
            if (!chromeRoot.exists()) {
                chromeRoot.mkdirs()
            }
            val chromeDriverUri = milestone.getMap("downloads")!!.getMapList("chromedriver")
                .first { it.getString("platform") == platform }.getString("url")!!
            var chromeDriverZip = downloadFile(URI.create(chromeDriverUri), File(chromeVersionPath, "#chromedriver.zip"))
            val chromeUri = milestone.getMap("downloads")!!.getMapList("chrome")
                .first { it.getString("platform") == platform }.getString("url")!!
            var chromeZip = downloadFile(URI.create(chromeDriverUri), File(chromeVersionPath, "#chrome.zip"))


            //downloadFile(URI.create(), File())




        } catch (e: Exception) {
          throw SeleniumChromeAllInOneException("failed to get versions $chromeVersion in $chromeDownloadUri", e)
        }
    }

    private fun downloadFile(uri: URI, file: File): File {
        return file
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
            }?.absolutePath ?: chromeVersionDirPrefix
    }

    companion object {
        fun create(root: File, platform: Platform, downloadOption: ChromeDownloadOption): ChromeLoader {
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
            return ChromeLoader(platform.value, platformRoot, binaryFileExt, downloadOption)
        }
    }
}