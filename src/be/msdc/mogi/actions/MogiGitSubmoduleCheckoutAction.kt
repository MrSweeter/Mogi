package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.GitSubmoduleCheckoutCommand
import be.msdc.mogi.settings.MogiSettings
import com.intellij.openapi.actionSystem.AnActionEvent

class MogiGitSubmoduleCheckoutAction : AnMogiAction() {

    override fun actionPerformed(event: AnActionEvent) {
        run(event, GitSubmoduleCheckoutCommand(MogiSettings.getInstance().checkoutGitBranch))
    }
}