package test

import me.saro.selenium.SeleniumChromeAllInOne
import me.saro.selenium.comm.ChromeDownloadOption
import me.saro.selenium.comm.Platform
import org.junit.jupiter.api.Test
import java.io.File

class StartTest {

    @Test
    fun test() {

        SeleniumChromeAllInOne.download(File("./tmp"), Platform.WINDOWS_64, ChromeDownloadOption.IF_MAJOR_VERSIONS_DIFFER_DOWNLOAD)
    }
}