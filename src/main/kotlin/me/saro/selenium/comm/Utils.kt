package me.saro.selenium.comm

import java.io.File
import java.util.logging.Logger
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.reflect.KClass

class Utils {
    companion object {
        fun <T : Any> getLogger(clazz: KClass<T>): Logger =
            Logger.getLogger(clazz.java.name)

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

        fun unzip(zipFile: File, zipRootDepth: Int, destDir: File) =
            unzip(zipFile) {
                val paths = it.name.split("/")
                if (paths.size > zipRootDepth) {
                    File(destDir, paths.drop(zipRootDepth).joinToString("/"))
                } else {
                    null
                }
            }

        fun unzip(zipFile: File, destDir: File) =
            unzip(zipFile) { File(destDir, it.name) }

        fun unzip(zipFile: File, eachSavePath: (ZipEntry) -> File?) =
            ZipFile(zipFile).use { zip -> zip.entries().asSequence().forEach { entry ->
                eachSavePath(entry)?.also { file ->
                    if (entry.isDirectory) {
                        file.mkdirs()
                    } else {
                        file.parentFile.mkdirs()
                        file.outputStream().use { output -> zip.getInputStream(entry).copyTo(output) }
                    }
                }
            }}
    }
}
