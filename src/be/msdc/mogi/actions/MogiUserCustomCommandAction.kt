package be.msdc.mogi.actions

import be.msdc.mogi.models.commands.UserCustomCommand
import be.msdc.mogi.settings.MogiSettings
import com.intellij.openapi.actionSystem.AnActionEvent

class MogiUserCustomCommandAction : AnMogiAction() {

    override fun actionPerformed(event: AnActionEvent) {
        run(event, UserCustomCommand(MogiSettings.getInstance().userCustomCommand))
    }
}