package me.maxos.votive.dangerMine.utils

import java.util.logging.Logger

object Debuger {

	var debugStatus: Boolean = true

	private val debug = Logger.getLogger("DangerMine Debug")

	fun sendDebug(msg: String) { if (debugStatus) debug.info(msg) }
	fun sendErrorDebug(msgErr: String) { if (debugStatus) debug.severe(msgErr) }

	fun debugUnit(unit: Unit) {
		run { unit }
	}
}