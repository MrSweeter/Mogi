package be.msdc.mogi.utils

class MogiException(message: String, val stack: String? = null): Exception(message) {
}