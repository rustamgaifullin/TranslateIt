package com.rm.translateit.api.logger

open class Logger {
    open fun info(message: String) {}
    open fun debug(message: String) {}
    open fun warning(message: String) {}
    open fun error(message: String) {}
    open fun verbose(message: String) {}
}