package be.msdc.mogi.utils

import be.msdc.mogi.settings.MogiConfigurable
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project

object NotificationManager {

    val mogiGroup = NotificationGroup.balloonGroup("be.msdc.mogi")

    fun info(title: String, desc: String, project: Project) {
        mogiGroup.createNotification("Mogi", title, desc, NotificationType.INFORMATION, null).notify(project)
    }

    fun warning(title: String, desc: String, project: Project) {
        mogiGroup.createNotification("Mogi", title, desc, NotificationType.WARNING, null).notify(project)
    }

    fun error(ex: MogiException, project: Project, linkSettings: Boolean = true) {
        val n = mogiGroup.createNotification(
            "Mogi",
            "Exception",
            ex.localizedMessage + "\n\n" + ex.stack,
            NotificationType.ERROR,
            null
        )
        if (linkSettings) {
            n.addAction(object : AnAction("Open settings") {
                override fun actionPerformed(p0: AnActionEvent) {
                    ShowSettingsUtil.getInstance().showSettingsDialog(
                        project,
                        MogiConfigurable::class.java
                    )
                }
            })
        }
        n.notify(project)
    }

}