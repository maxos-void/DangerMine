package me.maxos.votive.dangerMine.model

import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class Time(
	val startTime: LocalTime,
	val endTime: LocalTime,
) {
	val range = startTime..endTime
}
