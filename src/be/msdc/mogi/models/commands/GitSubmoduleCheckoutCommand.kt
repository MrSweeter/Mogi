package be.msdc.mogi.models.commands

class GitSubmoduleCheckoutCommand(private val gitBranch: String) : GitCommand() {
    override val name: String = "Submodule checkout"

    override fun getArgs(): List<String> {
        return listOf("submodule", "foreach", "git checkout ${gitBranch.trim()}")
    }
}