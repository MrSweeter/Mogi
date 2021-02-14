package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.GitCommand
import be.msdc.mogi.models.commands.GitSubmoduleUpdateCommand
import be.msdc.mogi.settings.MogiSettings
import com.intellij.openapi.actionSystem.AnActionEvent

class MogiGitSubmoduleUpdateAction : AnGitAction() {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            project.basePath?.let { path ->

                val cmd: GitCommand
                MogiSettings.getInstance().let {
                    cmd = GitSubmoduleUpdateCommand(it.useInit, it.useCheckout, it.useForce, it.useRecursive)
                }

                run(cmd, path, project)
            }
        }
    }
}