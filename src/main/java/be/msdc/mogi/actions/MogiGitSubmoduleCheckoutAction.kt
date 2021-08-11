package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.GitSubmoduleCheckoutCommand
import be.msdc.mogi.settings.MogiSettings
import be.msdc.mogi.utils.IntellijAction
import be.msdc.mogi.utils.ProcessRunner
import be.msdc.mogi.utils.getMogiString
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class MogiGitSubmoduleCheckoutAction : AnMogiAction() {

    override fun actionPerformed(event: AnActionEvent) {
        MogiSettings.getInstance().let {

            var branch: String? = it.checkoutGitBranch
            if (it.checkoutGitBranchRequest) {
                branch = Messages.showInputDialog(
                    getMogiString("command.git.checkout.dialog.message"),
                    getMogiString("command.git.checkout.dialog.title"),
                    Messages.getQuestionIcon(),
                    branch,
                    null
                )
            }
            if (!branch.isNullOrEmpty()) {
                run(event, GitSubmoduleCheckoutCommand(branch))
                if (it.useSync) {
                    ProcessRunner.run(IntellijAction.ANDROID_GRADLE_SYNC, event)
                }
            }
        }
    }
}