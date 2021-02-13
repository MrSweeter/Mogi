package be.msdc.mogi.utils

data class MogiResult(val success: String, val fail: String)    {
    fun isSuccess(): Boolean   {
        return fail.isEmpty()
    }
}
