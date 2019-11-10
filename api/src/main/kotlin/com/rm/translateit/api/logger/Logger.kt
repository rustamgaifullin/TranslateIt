package com.rm.translateit.api.logger

interface Logger {
  fun info(message: String)
  fun debug(message: String)
  fun warning(message: String)
  fun error(message: String)
  fun verbose(message: String)
}