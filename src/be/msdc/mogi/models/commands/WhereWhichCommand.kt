package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType

class WhereWhichCommand(private val toSearch: String) : MogiCommand() {

    override val name: String = "Where/Which"
    override val type: ProcessType = ProcessType.WHERE_WHICH

    override fun getArgs(): List<String> {
        return listOf(toSearch)
    }
}