package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType
import be.msdc.mogi.settings.MogiSettings
import be.msdc.mogi.utils.MogiException
import be.msdc.mogi.utils.getMogiString
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
        return getMogiString("command.toString", name, getCommandLine(null).commandLineString)
    }

    protected open fun setupCommandLine(workingDirectory: String?): GeneralCommandLine {
        val fullArgs = listOf(getCommand()) + getArgs()
        val cmd = GeneralCommandLine(fullArgs)
        cmd.charset = StandardCharsets.UTF_8
        workingDirectory?.let { cmd.workDirectory = File(it) }
        return cmd
        //cmd.exePath = getCommand(process)
    }

    @Throws(MogiException::class)
    open fun getCommand(): String {
        val result = forceExecutable ?: when (type) {
            ProcessType.WHERE_WHICH -> MogiSettings.getInstance().whereWhichPath
            ProcessType.GIT -> MogiSettings.getInstance().gitPath
            else -> ""
        }

        if (!type.isValid(result)) {
            if (result.isEmpty()) throw MogiException(getMogiString("error.empty.path", type.toString()))
            throw MogiException(getMogiString("error.invalid", result, type.toString()))
        }

        return result
    }
}