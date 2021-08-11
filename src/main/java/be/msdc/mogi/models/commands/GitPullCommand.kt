package be.msdc.mogi.models.commands

import be.msdc.mogi.utils.getMogiString

class GitPullCommand : GitCommand() {
    override val name: String = getMogiString("command.git.pull.name")

    override fun getArgs(): List<String> {
        return listOf("pull", "origin")
    }
}