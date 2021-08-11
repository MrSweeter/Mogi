package be.msdc.mogi.utils

import be.msdc.mogi.settings.MogiConfigurable
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project

object MogiNotificationManager {

    private val mogiGroup = NotificationGroupManager.getInstance().getNotificationGroup("Mogi Notification Group")

    fun info(title: String, desc: String, project: Project) {
        with(mogiGroup.createNotification(getMogiString("app.name"), desc, NotificationType.INFORMATION)) {
            subtitle = title
            notify(project)
        }
    }

    fun warning(title: String, desc: String, project: Project) {
        with(mogiGroup.createNotification(getMogiString("app.name"), desc, NotificationType.WARNING)) {
            subtitle = title
            notify(project)
        }
    }

    fun error(ex: MogiException, project: Project, linkSettings: Boolean = true) {
        var content = ex.localizedMessage
        if (!ex.stack.isNullOrEmpty()) content += "\n\n" + ex.stack
        with(mogiGroup.createNotification(getMogiString("app.name"), content, NotificationType.ERROR)) {
            subtitle = getMogiString("error.exception")
            if (linkSettings) {
                addAction(object : AnAction(getMogiString("error.link.settings")) {
                    override fun actionPerformed(p0: AnActionEvent) {
                        ShowSettingsUtil.getInstance().showSettingsDialog(
                            project,
                            MogiConfigurable::class.java
                        )
                    }
                })
            }
            notify(project)
        }
    }

}