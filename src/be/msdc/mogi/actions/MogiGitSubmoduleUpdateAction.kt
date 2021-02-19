package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.GitSubmoduleUpdateCommand
import be.msdc.mogi.models.commands.GradlewSyncCommand
import be.msdc.mogi.settings.MogiSettings
import com.intellij.openapi.actionSystem.AnActionEvent

class MogiGitSubmoduleUpdateAction : AnMogiAction() {

    override fun actionPerformed(event: AnActionEvent) {
        MogiSettings.getInstance().let {
            run(event, GitSubmoduleUpdateCommand(it.useInit, it.useCheckout, it.useForce, it.useRecursive))
            if (it.useSync && it.gradlewPath.isNotEmpty()) {
                run(event, GradlewSyncCommand(event.project?.basePath ?: ""))
            }
        }
    }
}