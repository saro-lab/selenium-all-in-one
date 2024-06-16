package me.saro.selenium.comm

import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter
import kotlin.reflect.KClass

class Utils {
    companion object {
        fun <T : Any> getLogger(clazz: KClass<T>): Logger =
            Logger.getLogger(clazz.java.name)
                .apply {
                    val consoleHandler = ConsoleHandler()
                    consoleHandler.formatter = SimpleFormatter()
                    addHandler(consoleHandler)
                    level = Level.ALL
                    consoleHandler.level = Level.ALL
                }
    }
}
