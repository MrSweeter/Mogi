package be.msdc.mogi.models.commands

class GitSubmoduleUpdateCommand(
    private val isGitInit: Boolean,
    private val isCheckout: Boolean,
    private val isForce: Boolean,
    private val isRecursive: Boolean
) : GitCommand() {

    override val name: String = "Submodule Update"

    override fun getArgs(): List<String> {
        val args = mutableListOf("submodule", "update")
        if (isGitInit) args.add("--init")
        if (isCheckout) args.add("--checkout")
        if (isForce) args.add("--force")
        if (isRecursive) args.add("--recursive")
        return args
    }

}