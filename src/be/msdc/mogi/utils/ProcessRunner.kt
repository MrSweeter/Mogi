package be.msdc.mogi.utils

import be.msdc.mogi.settings.MogiSettings
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessNotCreatedException
import com.intellij.execution.process.ProcessOutput
import java.io.File
import java.nio.charset.StandardCharsets

object ProcessRunner {

    @Throws(MogiException::class)
    fun run(process: String, args: List<String>): MogiResult {
        return run(getCommandLine(process, null, args))
    }

    @Throws(MogiException::class)
    fun run(process: ProcessType, workingDirectory: String, args: List<String>): MogiResult {
        return run(getCommandLine(process, workingDirectory, args))
    }

    @Throws(MogiException::class)
    private fun run(command: GeneralCommandLine): MogiResult    {
        val out: ProcessOutput
        try {
            out = CapturingProcessHandler(command).runProcess()
            return MogiResult(out.stdout, out.stderr)
        } catch (e: MogiException)  {
            throw e
        } catch (e: ProcessNotCreatedException) {
            throw MogiException("Error: " + command.commandLineString + "\n" + e.localizedMessage)
        } catch (e: Exception)  {
            throw MogiException("Error: " + command.commandLineString + "\n" + e.localizedMessage, e.stackTrace.toList().subList(0, 10).joinToString("\n"))
        }
    }

    @Throws(MogiException::class)
    private fun getCommandLine(process: ProcessType, workingDirectory: String?, args: List<String>): GeneralCommandLine {
        return getCommandLine(getCommand(process), workingDirectory, args)
    }

    private fun getCommandLine(process: String, workingDirectory: String?, args: List<String>): GeneralCommandLine {
        val fullArgs = listOf(process) + args
        val cmd = GeneralCommandLine(fullArgs)
        cmd.charset = StandardCharsets.UTF_8
        workingDirectory?.let { cmd.workDirectory = File(it) }
        return cmd
        //cmd.exePath = getCommand(process)
    }

    @Throws(MogiException::class)
    private fun getCommand(type: ProcessType): String {
        val result = when(type)  {
            ProcessType.WHERE -> MogiSettings.getInstance().whereWhichPath
            ProcessType.GIT -> MogiSettings.getInstance().gitPath
        }

        if (!type.isValid(result))   {
            if (result.isEmpty()) throw MogiException("Empty path for $type command")
            throw MogiException("$result is not a valid path for $type command")
        }

        return result
    }
}