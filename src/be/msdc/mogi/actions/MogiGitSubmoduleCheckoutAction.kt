package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.GitCommand
import be.msdc.mogi.models.commands.GitSubmoduleCheckoutCommand
import be.msdc.mogi.settings.MogiSettings
import com.intellij.openapi.actionSystem.AnActionEvent

class MogiGitSubmoduleCheckoutAction : AnGitAction() {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            project.basePath?.let { path ->

                val cmd: GitCommand
                MogiSettings.getInstance().let {
                    cmd = GitSubmoduleCheckoutCommand(it.checkoutGitBranch)
                }

                run(cmd, path, project)
            }
        }
    }
}