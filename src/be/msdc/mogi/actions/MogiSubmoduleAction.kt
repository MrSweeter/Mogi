package be.msdc.mogi.actions

import be.msdc.mogi.settings.MogiSettings
import be.msdc.mogi.utils.MogiException
import be.msdc.mogi.utils.NotificationManager
import be.msdc.mogi.utils.ProcessRunner
import be.msdc.mogi.utils.ProcessType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class MogiSubmoduleAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        project?.let { p ->
            p.basePath?.let { path ->

                val args = mutableListOf("submodule", "update")
                MogiSettings.getInstance().let {
                    if (it.useInit) args.add("--init")
                    if (it.useCheckout) args.add("--checkout")
                    if (it.useForce) args.add("--force")
                    if (it.useRecursive) args.add("--recursive")
                }

                try {
                    val result = ProcessRunner.run(ProcessType.GIT, path, args)
                    if (result.isSuccess()) {
                        NotificationManager.info("git submodule success", result.success, p)
                    } else {
                        NotificationManager.warning("git submodule fail", result.fail, p)
                    }

                } catch (ex: MogiException) {
                    NotificationManager.error(ex, p)
                }
            }
        }
    }
}