package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType
import be.msdc.mogi.settings.MogiSettings
import be.msdc.mogi.utils.MogiException
import com.intellij.execution.configurations.GeneralCommandLine
import java.io.File
import java.nio.charset.StandardCharsets

abstract class MogiCommand {

    var forceExecutable: String? = null
    abstract val type: ProcessType
    abstract val name: String

    open fun getArgs(): List<String> {
        return emptyList()
    }

    @Throws(MogiException::class)
    fun getCommandLine(workingDirectory: String?): GeneralCommandLine {
        return setupCommandLine(workingDirectory)
    }

    override fun toString(): String {
        return "[$name] " + getCommandLine(null).commandLineString
    }

    private fun setupCommandLine(workingDirectory: String?): GeneralCommandLine {
        val fullArgs = listOf(getCommand()) + getArgs()
        val cmd = GeneralCommandLine(fullArgs)
        cmd.charset = StandardCharsets.UTF_8
        workingDirectory?.let { cmd.workDirectory = File(it) }
        return cmd
        //cmd.exePath = getCommand(process)
    }

    @Throws(MogiException::class)
    fun getCommand(): String {
        val result = forceExecutable ?: when (type) {
            ProcessType.WHERE_WHICH -> MogiSettings.getInstance().whereWhichPath
            ProcessType.GIT -> MogiSettings.getInstance().gitPath
        }

        if (!type.isValid(result)) {
            if (result.isEmpty()) throw MogiException("Empty path for $type command")
            throw MogiException("$result is not a valid path for $type command")
        }

        return result
    }
}