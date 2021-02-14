package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.GitCommand
import be.msdc.mogi.utils.MogiException
import be.msdc.mogi.utils.NotificationManager
import be.msdc.mogi.utils.ProcessRunner
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project

abstract class AnGitAction : AnAction() {

    fun run(cmd: GitCommand, workingDirectory: String, project: Project) {
        try {
            val result = ProcessRunner.run(cmd, workingDirectory)
            if (result.isSuccess()) {
                NotificationManager.info("${cmd.name}: success", result.success, project)
            } else {
                NotificationManager.warning("${cmd.name}: fail", result.fail, project)
            }

        } catch (ex: MogiException) {
            NotificationManager.error(ex, project)
        }
    }

}