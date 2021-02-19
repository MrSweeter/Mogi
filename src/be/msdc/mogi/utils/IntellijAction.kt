package be.msdc.mogi.utils

enum class IntellijAction(val value: String) {

    UPDATE_PROJECT("Vcs.UpdateProject"),
    ANDROID_GRADLE_SYNC("Android.SyncProject"),

    MOGI_SUBMODULE_UPDATE("Mogi.SubmoduleUpdateAction");
}