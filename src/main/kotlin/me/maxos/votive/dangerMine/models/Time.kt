package me.maxos.votive.dangerMine.models

import java.time.LocalTime

data class Time(
	val startTime: LocalTime,
	val endTime: LocalTime,
) {
	val range = startTime..endTime
}
