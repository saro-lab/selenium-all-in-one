package me.saro.selenium.comm

import java.net.URI

class ChromeDownloadInfo(
    private val uri: URI,
    private val platform: String,
    val chromeVersion: String,
) {
    private val log = Utils.getLogger(ChromeDownloadInfo::class)
    val revision: String
    val chromeDriverUri: String
    val chromeUri: String

    init {
        val map = MapHelper.byURI(uri)
        val milestone = map.getMap("milestones")!!.getMap(chromeVersion)!!
        this.revision = milestone.getString("revision", "")
        this.chromeDriverUri = milestone.getMap("downloads")!!.getMapList("chromedriver").first { it.getString("platform") == platform }.getString("url")!!
        this.chromeUri = milestone.getMap("downloads")!!.getMapList("chrome").first { it.getString("platform") == platform }.getString("url")!!
    }
}