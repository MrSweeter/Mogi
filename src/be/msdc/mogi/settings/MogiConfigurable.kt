package be.msdc.mogi.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class MogiConfigurable : Configurable {

    private var panel: MogiSettingsPanel? = null

    override fun createComponent(): JComponent? {
        panel = MogiSettingsPanel()
        panel?.load(MogiSettings.getInstance())
        return panel?.panel
    }

    override fun isModified(): Boolean {
        return panel?.isModified(MogiSettings.getInstance()) == true
    }

    override fun apply() {
        panel?.save(MogiSettings.getInstance())
    }

    override fun reset() {
        panel?.load(MogiSettings.getInstance())
    }

    override fun getDisplayName(): String {
        return "Mogi Settings"
    }
}