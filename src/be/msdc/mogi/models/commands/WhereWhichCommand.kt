package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType
import be.msdc.mogi.utils.getMogiString

class WhereWhichCommand(private val toSearch: String) : MogiCommand() {

    override val name: String = getMogiString("command.whereWhich.name")
    override val type: ProcessType = ProcessType.WHERE_WHICH

    override fun getArgs(): List<String> {
        return listOf(toSearch)
    }
}