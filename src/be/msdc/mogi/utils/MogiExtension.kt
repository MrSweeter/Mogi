package be.msdc.mogi.utils

import java.util.*

fun getMogiString(key: String, vararg args: String): String {
    if (args.isEmpty()) return ResourceBundle.getBundle("mogi").getString(key)
    return String.format(ResourceBundle.getBundle("mogi").getString(key), args)
}