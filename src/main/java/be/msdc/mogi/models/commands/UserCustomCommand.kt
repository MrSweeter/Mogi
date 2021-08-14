package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType
import be.msdc.mogi.utils.MogiException
import be.msdc.mogi.utils.getMogiString
import com.intellij.execution.configurations.GeneralCommandLine
import java.io.File
import java.nio.charset.StandardCharsets

class UserCustomCommand(private val userCommand: String) : MogiCommand() {

    override val name: String = getMogiString("command.custom.name")
    override val type: ProcessType = ProcessType.CUSTOM

    override fun getArgs(): List<String> {
        val result = userCommand.split(" ")
        result.forEach { it.replace("%20", " ") }
        return result
    }

    private fun getValidCommand(): String {
        if (!type.isValid(userCommand)) {
            if (userCommand.isEmpty()) throw MogiException(getMogiString("error.empty.path", type.toString()))
            throw MogiException(getMogiString("error.invalid", userCommand, type.toString()))
        }
        return userCommand
    }

    override fun setupCommandLine(workingDirectory: String?): GeneralCommandLine {
        getValidCommand()

        val fullArgs = getArgs()
        val cmd = GeneralCommandLine(fullArgs)
        cmd.charset = StandardCharsets.UTF_8
        workingDirectory?.let { cmd.workDirectory = File(it) }
        return cmd
        //cmd.exePath = getCommand(process)
    }

    override fun getCommand(): String {
        return getValidCommand()
    }

}