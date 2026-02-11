package me.maxos.votive.dangerMine.extensions

import java.time.Duration


object DurationExtension {

	fun format(hours: Int, minutes: Int): String = "${hours}:${minutes}"

	fun Duration?.formatted(): String? {
		if (this == null) return null
		return "${toHours()}:${String.format("%02d", toMinutesPart())}"
	}
}