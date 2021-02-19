package be.msdc.mogi.models.commands

import be.msdc.mogi.models.ProcessType

class GradlewVersionCommand(private val workingDirectory: String?) : MogiCommand() {

    override val name: String = "Gradlew Version"
    override val type: ProcessType = ProcessType.GRADLEW

    override fun getArgs(): List<String> {
        return listOf("-v")
    }

    override fun toString(): String {
        return "[$name] " + getCommandLine(workingDirectory).commandLineString
    }
}