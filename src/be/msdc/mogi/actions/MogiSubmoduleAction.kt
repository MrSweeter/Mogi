package be.msdc.mogi.actions

import be.msdc.mogi.settings.MogiSettings
import be.msdc.mogi.utils.MogiException
import be.msdc.mogi.utils.NotificationManager
import be.msdc.mogi.utils.ProcessRunner
import be.msdc.mogi.utils.ProcessType
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.*
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.terminal.TerminalExecutionConsole
import com.intellij.util.io.BaseDataReader.SleepingPolicy
import com.intellij.util.io.BaseOutputReader
import com.jetbrains.rd.util.string.PrettyPrinter
import com.jetbrains.rd.util.string.print
import org.jetbrains.annotations.NotNull
import java.io.File
import java.nio.charset.Charset


class MogiSubmoduleAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        project?.let { p ->
            p.basePath?.let { path ->

                val args = mutableListOf("submodule", "update")
                MogiSettings.getInstance().let {
                    if (it.useInit) args.add("--init")
                    if (it.useCheckout) args.add("--checkout")
                    if (it.useForce) args.add("--force")
                    if (it.useRecursive) args.add("--recursive")
                }

                try {
                    val result = ProcessRunner.run(ProcessType.GIT, path, args)
                    if (result.isSuccess()) {
                        NotificationManager.info("git submodule success", result.success, p)
                    } else {
                        NotificationManager.warning("git submodule fail", result.fail, p)
                    }

                } catch (ex: MogiException) {
                    NotificationManager.error(ex, p)
                }
            }
        }
    }
}