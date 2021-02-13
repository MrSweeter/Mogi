package be.msdc.mogi.utils

import com.intellij.openapi.util.SystemInfo

enum class ProcessType(private val win: String, private val mac: String) {
    WHERE("where.exe", "which"),
    GIT("git.exe", "git");

    fun isValid(path: String): Boolean  {
        return path.trim().endsWith(getExecutableName())
    }

    fun getExecutableName(): String {
        return if (SystemInfo.isWindows) win else mac
    }
}