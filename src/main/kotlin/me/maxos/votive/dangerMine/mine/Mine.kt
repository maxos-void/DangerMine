package me.maxos.votive.dangerMine.mine

import me.maxos.votive.dangerMine.mine.manager.TimerManager
import me.maxos.votive.dangerMine.model.MineSchema
import me.maxos.votive.dangerMine.model.Time
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import java.time.Duration
import java.time.LocalTime
import kotlin.properties.Delegates

class Mine(
	val schema: MineSchema,
	private val timerManager: TimerManager
) {
	// статус шахты, если открыта - true
	var isOpen: Boolean = false
		private set

	// активна ли шахта в сегодняшний день
	var isActiveToday by Delegates.notNull<Boolean>()
	init { updateDate() }
	fun updateDate() {
		isActiveToday = timerManager.currentDate in schema.workDate || schema.isAnyDate
	}

	private val currentLocalTime: LocalTime get() = timerManager.currentTime
	private val actualTime: Time? get() = getCurrentTimeRange()
	private val nextTime: Time?
		get() = schema.times
			.filter {
				it.startTime.isAfter(timerManager.currentTime)
			}
			.minByOrNull {
				it.startTime
			}

	val beforeClosing: Duration? get() = Duration.between(currentLocalTime, actualTime?.endTime ?: return null)
	val beforeOpen: Duration? get() = Duration.between(currentLocalTime, nextTime?.startTime ?: return null)


	fun ping() {
		if (!isActiveToday) return
		when (isOpen) {
			true -> {
				if (actualTime == null) {
					close()
				} else sendDebug("Шахта ${schema.name} закроется через: ${beforeClosing?.toMinutes()?.toInt()}")
			}

			false -> {
				if (actualTime != null) {
					open()
				} else sendDebug("Шахта ${schema.name} откроется через: ${beforeOpen?.toMinutes()?.toInt()}")
			}
		}
	}

	private fun open() {
		sendDebug("Шахта ${schema.name} открылась!")
		isOpen = true
	}

	private fun close() {
		sendDebug("Шахта ${schema.name} закрылась!")
		isOpen = false
	}

	private fun getCurrentTimeRange(): Time? = schema.times
		.firstOrNull {
			currentLocalTime in it.range
		}
}
	/*
	// Когда закрывать (вычисляем при открытии)
	private var activeTime: Time? = null
	private val closeAt: LocalTime? get() = activeTime?.endTime


	fun ping() {
		when (isOpen) {
			true -> {
				closeAt?.let {
					if (it == timerManager.currentTime) {
						close()
						sendDebug("Шахта ${schema.name} закрылась!")
					} else sendDebug(
						"Шахте ${schema.name} до закрытия: ${closeDuration()?.toMinutes()?.toInt()} минут!"
					)
				}
			}
			false -> {
				startDuration()?.toMinutes()?.toInt()?.let {
					if (it == 0) {
						open()
						sendDebug("Шахта ${schema.name} открылась!")
					} else sendDebug("Шахте ${schema.name} до открытия: $it минут!")
				} ?: run {
					sendDebug("Шахте ${schema.name} не удалось высчитать время ожидания! (waitingTime = null)")
					return
				}
			}
		}
	}

	private fun open() {
		activeTime = getCurrentTimeRange()
		isOpen = true
	}
	private fun close() {
		isOpen = false
	}

	private fun getCurrentTimeRange(): Time? {
		val now = timerManager.currentTime
		return schema.times.firstOrNull {
			now in it.startTime..it.endTime
		}
	}

	private fun duration(firstTime: LocalTime?, secondTime: LocalTime?): Duration? {
		return Duration.between(
			//timerManager.currentTime,
			//nearestTime()?.startTime ?: return null
			firstTime ?: return null, secondTime ?: return null
		)
	}
	fun startDuration() = duration(timerManager.currentTime, nearestTime()?.startTime)
	fun closeDuration() = duration(timerManager.currentTime, activeTime?.endTime)

	fun nearestTime(): Time? {
		return schema.times
			.filter {
				it.startTime.isAfter(timerManager.currentTime)
			}
			.minByOrNull {
				it.startTime
			}
	}

	fun nextDay(): Int? {
		return when {
			schema.isAnyDate -> 0
			else -> schema.workDate
				.filter { it > timerManager.currentDate }
				.minOrNull()
		}
	}
	 */
