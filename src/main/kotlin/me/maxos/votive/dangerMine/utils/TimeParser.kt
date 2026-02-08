package me.maxos.votive.dangerMine.utils

import java.time.LocalTime
import kotlin.text.split

object TimeParser {

	fun String.toTimer(): Pair<LocalTime, LocalTime>? {
		val times = this.split('-')
		val startTime = getLocalTime(times.getOrNull(0) ?: return null) ?: return null
		val endTime = getLocalTime(times.getOrNull(1) ?: return null) ?: return null
		return Pair(startTime, endTime)
	}

	private fun getLocalTime(time: String): LocalTime? {
		time.split(':').let { parts ->
			val hour = parts.getOrNull(0)?.toIntOrNull()?.takeIf { it in 0..23 } ?: run { return null }
			val minute = parts.getOrNull(1)?.toIntOrNull()?.takeIf { it in 0..59 } ?: return null
			return LocalTime.of(hour, minute)
		}
	}
}