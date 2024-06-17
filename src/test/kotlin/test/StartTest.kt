package test

import me.saro.selenium.SeleniumChromeAllInOne
import me.saro.selenium.model.ChromeDownloadOption
import org.junit.jupiter.api.Test
import java.io.File

class StartTest {

    @Test
    fun test() {

        //SeleniumChromeAllInOne.download(File("./tmp"), Platform.WINDOWS_64, ChromeDownloadOption.IF_MAJOR_VERSIONS_DIFFER_DOWNLOAD)

        //SeleniumChromeAllInOne.download(File("./tmp"), Platform.WINDOWS_64, ChromeDownloadOption.IF_MINOR_VERSIONS_DIFFER_DOWNLOAD)


        var sca = SeleniumChromeAllInOne.builder(File("./tmp"))
            .enableRecommendChromeOptions(true)
            .chromeDownloadOption(ChromeDownloadOption.IF_MINOR_VERSIONS_DIFFER_DOWNLOAD)
            .build()


    }
}