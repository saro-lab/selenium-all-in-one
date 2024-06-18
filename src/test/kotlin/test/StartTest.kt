package test

import me.saro.selenium.SeleniumChromeAllInOne
import me.saro.selenium.model.DownloadStrategy
import org.junit.jupiter.api.Test
import java.io.File

class StartTest {

    @Test
    fun test() {

        //SeleniumChromeAllInOne.download(File("./tmp"), Platform.WINDOWS_64, ChromeDownloadOption.IF_MAJOR_VERSIONS_DIFFER_DOWNLOAD)

        //SeleniumChromeAllInOne.download(File("./tmp"), Platform.WINDOWS_64, ChromeDownloadOption.IF_MINOR_VERSIONS_DIFFER_DOWNLOAD)


        var sca = SeleniumChromeAllInOne.builder(File("./tmp"))
            .enableRecommendChromeOptions(true)
            .chromeDownloadOption(DownloadStrategy.DOWNLOAD_IF_NO_VERSION_OR_DIFFERENT_REVISION)
            .build()

        sca.openBackground("https://anissia.net") {

            driver.finds(".flex.items-center.py-3.my-1.border-b.border-gray-200.text-sm.anissia-home-reduce-10").forEach {
                println(it.find("a").text)
            }

        }

    }
}