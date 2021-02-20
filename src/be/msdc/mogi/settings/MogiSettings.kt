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

    companion object {
        fun getInstance(): MogiSettings = ServiceManager.getService(MogiSettings::class.java)
    }

    // Where / Which
    var whereWhichPath: String = ""

    // Git
    var useInit: Boolean = false
    var useForce: Boolean = true
    var useCheckout: Boolean = true
    var useRecursive: Boolean = true
    var useSync: Boolean = false
    var useIntellijPull: Boolean = false
    var checkoutGitBranch: String = "master"
    var gitPath: String = ""

    // Custom
    var userCustomCommand: String = ""

    override fun getState(): MogiSettings {
        return this
    }

    override fun loadState(that: MogiSettings) {
        this.whereWhichPath = that.whereWhichPath

        this.useInit = that.useInit
        this.useForce = that.useForce
        this.useCheckout = that.useCheckout
        this.useRecursive = that.useRecursive
        this.useSync = that.useSync
        this.useIntellijPull = that.useIntellijPull
        this.checkoutGitBranch = that.checkoutGitBranch
        this.gitPath = that.gitPath

        this.userCustomCommand = that.userCustomCommand
    }
}