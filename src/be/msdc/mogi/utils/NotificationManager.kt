package be.msdc.mogi.utils

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object NotificationManager {

    val mogiGroup = NotificationGroup.balloonGroup("be.msdc.mogi")

    fun info(title: String, desc: String, project: Project)  {
        mogiGroup.createNotification("Mogi", title, desc, NotificationType.INFORMATION, null).notify(project)
    }

    fun warning(title: String, desc: String, project: Project)   {
        mogiGroup.createNotification("Mogi", title, desc, NotificationType.WARNING, null).notify(project)
    }

    fun error(ex: MogiException, project: Project) {
        mogiGroup.createNotification("Mogi", "Exception", ex.localizedMessage + "\n\n" + ex.stack, NotificationType.ERROR, null).notify(project)
    }

}