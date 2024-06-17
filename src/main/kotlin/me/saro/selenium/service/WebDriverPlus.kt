package me.saro.selenium.service

import me.saro.selenium.comm.Utils
import org.openqa.selenium.*
import java.time.Duration

class WebDriverPlus(
    val driver: WebDriver
) {
    private val log = Utils.getLogger(WebDriverPlus::class)

    fun <T> use(url: String, use: WebDriverPlus.() -> T): T {
        try {
            return use(this.apply { move(url) })
        } finally {
            try { driver.close() } catch (e: Exception) {}
            try { driver.quit() } catch (e: Exception) {}
            log.info("chrome driver closed.")
        }
    }

    fun move(url: String) {
        log.info("connect to $url")
        driver.get(url)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20))
    }
    fun scrollDown(size: Int) {
        script("window.scrollBy(0, $size)")
    }
    fun scrollDown() {
        script("window.scrollBy(0, document.body.scrollHeight)")
    }
    fun script(script: String): Any? =
        (driver as JavascriptExecutor).executeScript(script)
    fun sleep(millis: Long) =
        Thread.sleep(millis)

    fun SearchContext.find(css: String): WebElement = this.findElement(By.cssSelector(css))
    fun SearchContext.finds(css: String): List<WebElement> = this.findElements(By.cssSelector(css))

    fun SearchContext.findsNotWait(css: String): List<WebElement> {
        val before = driver.manage().timeouts().implicitWaitTimeout
        driver.manage().timeouts().implicitlyWait(Duration.ZERO)
        val list = this.finds(css)
        driver.manage().timeouts().implicitlyWait(before)
        return list
    }

    fun SearchContext.hasElementsNotWait(css: String): Boolean {
        return findsNotWait(css).isNotEmpty()
    }

    init {
        log.info("chrome driver opened.")
    }
}