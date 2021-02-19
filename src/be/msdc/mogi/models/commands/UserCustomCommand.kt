package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType
import be.msdc.mogi.utils.MogiException
import com.intellij.execution.configurations.GeneralCommandLine
import java.io.File
import java.nio.charset.StandardCharsets

class UserCustomCommand(private val userCommand: String) : MogiCommand() {

    override val name: String = "Custom Command"
    override val type: ProcessType = ProcessType.CUSTOM

    override fun getArgs(): List<String> {
        val result = userCommand.split(" ")
        result.forEach { it.replace("%20", " ") }
        return result
    }

    private fun getValidCommand(): String {
        if (!type.isValid(userCommand)) {
            if (userCommand.isEmpty()) throw MogiException("Empty path for $type command")
            throw MogiException("$userCommand is not a valid path for $type command")
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

    override fun getCommand(workingDirectory: String?): String {
        return getValidCommand()
    }

}