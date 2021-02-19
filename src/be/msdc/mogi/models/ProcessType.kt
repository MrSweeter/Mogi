package be.msdc.mogi.models

import com.intellij.openapi.util.SystemInfo

enum class ProcessType(private val win: String, private val mac: String) {
    WHERE_WHICH("where.exe", "which"),
    GIT("git.exe", "git"),
    GRADLEW("gradlew.bat", "gradlew"),
    CUSTOM("", "");

    fun isValid(path: String): Boolean {
        return path.isNotEmpty() && path.trim().endsWith(getExecutableName())
    }

    fun getExecutableName(): String {
        return if (SystemInfo.isWindows) win else mac
    }
}