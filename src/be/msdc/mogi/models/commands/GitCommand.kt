package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType

abstract class GitCommand : MogiCommand() {
    override val type: ProcessType = ProcessType.GIT
}