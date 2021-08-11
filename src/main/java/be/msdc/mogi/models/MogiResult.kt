package be.msdc.mogi.models

data class MogiResult(val success: String, val fail: String) {
    fun isSuccess(): Boolean {
        return fail.isEmpty()
    }
}
