package be.msdc.mogi.models.commands

import be.msdc.mogi.utils.getMogiString

class GitSubmoduleCheckoutCommand(private val gitBranch: String) : GitCommand() {
    override val name: String = getMogiString("command.git.checkout.name")

    override fun getArgs(): List<String> {
        return listOf("submodule", "foreach", "git checkout ${gitBranch.trim()}")
    }
}