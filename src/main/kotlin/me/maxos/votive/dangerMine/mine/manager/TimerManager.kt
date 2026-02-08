package me.maxos.votive.dangerMine.mine.manager

import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class TimerManager {

	private val moscowZone = ZoneId.of("Europe/Moscow")
	val currentTime: LocalTime get() = ZonedDateTime.now(moscowZone).toLocalTime().reset()

	var lastDate = ZonedDateTime.now(moscowZone).toLocalDate().dayOfMonth
	val currentDate: Int get() = ZonedDateTime.now(moscowZone).toLocalDate().dayOfMonth

	fun relevanceDate(): Boolean {
		if (lastDate == currentDate) return true
		else {
			lastDate = currentDate
			return false
		}
	}

	private fun LocalTime.reset() = this
		.withSecond(0)
		.withNano(0)
}