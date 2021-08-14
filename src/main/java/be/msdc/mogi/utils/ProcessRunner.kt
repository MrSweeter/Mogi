package be.msdc.mogi.utils

import be.msdc.mogi.models.MogiResult
import be.msdc.mogi.models.commands.MogiCommand
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessNotCreatedException
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent

object ProcessRunner {

    fun run(action: IntellijAction, event: AnActionEvent) {
        ActionManager.getInstance().getAction(action.value)?.actionPerformed(event)
    }

    @Throws(MogiException::class)
    fun run(command: MogiCommand, workingDirectory: String? = null): MogiResult {
        println(command.toString())
        return run(command.getCommandLine(workingDirectory))
    }

    @Throws(MogiException::class)
    private fun run(command: GeneralCommandLine): MogiResult {
        val out: ProcessOutput
        try {
            out = CapturingProcessHandler(command).runProcess()
            return MogiResult(out.stdout, out.stderr)
        } catch (e: MogiException) {
            throw e
        } catch (e: ProcessNotCreatedException) {
            throw MogiException(getMogiString("error.message", command.commandLineString + "\n" + e.localizedMessage))
        } catch (e: Exception) {
            throw MogiException(
                getMogiString("error.message", command.commandLineString + "\n" + e.localizedMessage),
                e.stackTrace.toList().subList(0, 10).joinToString("\n")
            )
        }
    }
}