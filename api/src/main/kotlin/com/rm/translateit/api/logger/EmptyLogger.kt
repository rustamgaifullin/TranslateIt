package com.rm.translateit.api.logger

internal class EmptyLogger : Logger {
  override fun info(message: String) {}

  override fun debug(message: String) {}

  override fun warning(message: String) {}

  override fun error(message: String) {}

  override fun verbose(message: String) {}
}