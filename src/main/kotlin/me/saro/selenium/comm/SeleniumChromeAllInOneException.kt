package me.saro.selenium.comm

class SeleniumChromeAllInOneException(
    override val message: String,
    override val cause: Throwable? = null
): RuntimeException() {
    constructor(cause: Throwable): this(cause.message?:"", cause)
    constructor(message: String): this(message, null)
}