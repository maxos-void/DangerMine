package me.maxos.votive.dangerMine.utils

import java.util.logging.Logger

private val log = Logger.getLogger("DangerMine")

fun logInfo(msg: String) { log.info(msg) }
fun logError(msgErr: String) { log.severe(msgErr) }