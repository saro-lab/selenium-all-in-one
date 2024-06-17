package me.saro.selenium.model

enum class ChromeDownloadOption {
    // just major version check or throw
    JUST_MAJOR_VERSION_CHECK_OR_THROW,
    // just major version check or download (default)
    IF_MAJOR_VERSIONS_DIFFER_DOWNLOAD,
    // just major version check or download
    IF_MINOR_VERSIONS_DIFFER_DOWNLOAD,
}