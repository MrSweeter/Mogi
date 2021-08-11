package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.MogiCommand
import be.msdc.mogi.utils.MogiException
import be.msdc.mogi.utils.NotificationManager
import be.msdc.mogi.utils.ProcessRunner
import be.msdc.mogi.utils.getMogiString
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

abstract class AnMogiAction : AnAction() {

    fun run(event: AnActionEvent, cmd: MogiCommand) {
        event.project?.let { project ->
            project.basePath?.let { workingDirectory ->
                try {
                    val result = ProcessRunner.run(cmd, workingDirectory)
                    if (result.isSuccess()) {
                        NotificationManager.info(
                            getMogiString("notification.command.success", cmd.name),
                            result.success,
                            project
                        )
                    } else {
                        NotificationManager.warning(
                            getMogiString("notification.command.fail", cmd.name),
                            cmd.toString() + "\n\n" + result.fail,
                            project
                        )
                    }

                } catch (ex: MogiException) {
                    NotificationManager.error(ex, project)
                }
            }
        }
    }

}