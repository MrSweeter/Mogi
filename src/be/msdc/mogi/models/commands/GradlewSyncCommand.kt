package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType

class GradlewSyncCommand(private val workingDirectory: String?) : MogiCommand() {

    override val name: String = "Gradlew Sync"
    override val type: ProcessType = ProcessType.GRADLEW

    override fun getArgs(): List<String> {
        return listOf("sync")
    }

    override fun toString(): String {
        return "[$name] " + getCommandLine(workingDirectory).commandLineString
    }
}