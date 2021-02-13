package be.msdc.mogi.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "Mogi",
    storages = [Storage("mogi-settings.xml")]
)
class MogiSettings : PersistentStateComponent<MogiSettings> {

    companion object    {
        fun getInstance(): MogiSettings = ServiceManager.getService(MogiSettings::class.java)
    }

    var whereWhichPath: String = ""
    var gitPath: String = ""
    var useInit: Boolean = true
    var useForce: Boolean = true
    var useCheckout: Boolean = true
    var useRecursive: Boolean = true

    override fun getState(): MogiSettings {
        return this
    }

    override fun loadState(that: MogiSettings) {
        this.whereWhichPath = that.whereWhichPath
        this.gitPath = that.gitPath
        this.useInit = that.useInit
        this.useForce = that.useForce
        this.useCheckout = that.useCheckout
        this.useRecursive = that.useRecursive
    }
}