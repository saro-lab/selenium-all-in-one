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

        fun getPlatform(): Platform {
            val os = System.getProperty("os.name").lowercase()
            val arch = System.getProperty("os.arch").lowercase()
            if (!arch.contains("64")) {
                throw RuntimeException("not supported $os / $arch")
            }
            return when {
                os.contains("linux") -> Platform.LINUX_64
                os.contains("windows") -> Platform.WINDOWS_64
                os.contains("mac") -> {
                    if (arch.contains("aarch64")) {
                        Platform.MAC_ARM64
                    } else {
                        Platform.MAC_X64
                    }
                }
                else -> throw RuntimeException("not supported $os / $arch")
            }
        }
    }
}
