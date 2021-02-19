package be.msdc.mogi.actions

import be.msdc.mogi.settings.MogiSettings
import be.msdc.mogi.utils.IntellijAction
import be.msdc.mogi.utils.ProcessRunner
import com.intellij.openapi.actionSystem.AnActionEvent

class MogiGitPullAndUpdateAction : AnMogiAction() {

    override fun actionPerformed(event: AnActionEvent) {
        MogiSettings.getInstance().let {
            ProcessRunner.run(IntellijAction.UPDATE_PROJECT, event)
            ProcessRunner.run(IntellijAction.MOGI_SUBMODULE_UPDATE, event)
        }
    }

}