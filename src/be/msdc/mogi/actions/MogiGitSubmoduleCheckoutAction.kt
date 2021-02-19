package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.GitSubmoduleCheckoutCommand
import be.msdc.mogi.models.commands.GradlewSyncCommand
import be.msdc.mogi.settings.MogiSettings
import com.intellij.openapi.actionSystem.AnActionEvent

class MogiGitSubmoduleCheckoutAction : AnMogiAction() {

    override fun actionPerformed(event: AnActionEvent) {
        MogiSettings.getInstance().let {
            run(event, GitSubmoduleCheckoutCommand(it.checkoutGitBranch))
            if (it.useSync && it.gradlewPath.isNotEmpty()) {
                run(event, GradlewSyncCommand(event.project?.basePath ?: ""))
            }
        }
    }
}