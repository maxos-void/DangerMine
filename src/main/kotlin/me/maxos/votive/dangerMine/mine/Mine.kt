package me.maxos.votive.dangerMine.mine

import me.maxos.votive.dangerMine.api.EventApiManager
import me.maxos.votive.dangerMine.api.customevent.MineCloseEvent
import me.maxos.votive.dangerMine.api.customevent.MineOpenEvent
import me.maxos.votive.dangerMine.file.config.ConfigManager
import me.maxos.votive.dangerMine.mine.action.MineAction
import me.maxos.votive.dangerMine.mine.manager.TimerManager
import me.maxos.votive.dangerMine.models.LiteLocation
import me.maxos.votive.dangerMine.models.MineSchema
import me.maxos.votive.dangerMine.models.Time
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import me.maxos.votive.dangerMine.utils.logInfo
import org.bukkit.Bukkit
import org.bukkit.Location
import java.time.Duration
import java.time.LocalTime
import kotlin.properties.Delegates

class Mine(
	val schema: MineSchema,
	private val timerManager: TimerManager,
	private val configManager: ConfigManager,
) {

	private fun callEvent(mineAction: MineAction) {
		when (mineAction) {
			MineAction.OPEN -> EventApiManager.callEvent(MineOpenEvent(this))
			MineAction.CLOSE -> EventApiManager.callEvent(MineCloseEvent(this))
		}
	}

	// статус шахты, если открыта - true
	var isOpen: Boolean = false
		private set

	// активна ли шахта в сегодняшний день
	var isActiveToday by Delegates.notNull<Boolean>()
	fun updateDate() {
		isActiveToday = timerManager.currentDate in schema.workDate || schema.isAnyDate
	}

	// локация, на которую будем телепортировать игрока при разных обстоятельствах
	// (вход в шахту)
	var entranceLoc: Location? = null
		private set

	private fun initLocation() {
		schema.liteLocation.let {
			val world = Bukkit.getWorld(it.world) ?: run {
				logInfo("Мир ${it.world} не найден! Не удалось создать локацию входа для шахты ${schema.id}")
				return
			}
			entranceLoc = Location(
				world,
				it.x,
				it.y,
				it.z,
				it.yaw,
				it.pitch
			)
		}
	}

	fun setLocation(loc: Location) {
		entranceLoc = loc
		LiteLocation(
			loc.world.name,
			loc.x,
			loc.y,
			loc.z,
			loc.yaw,
			loc.pitch
		).let {
			configManager.changeLoc(schema, it)
		}
	}


	init {
		updateDate()
		initLocation()
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
		logInfo("Шахта ${schema.id} открылась!")
		isOpen = true
		callEvent(MineAction.OPEN)
	}

	private fun close() {
		logInfo("Шахта ${schema.id} закрылась!")
		isOpen = false
		callEvent(MineAction.CLOSE)
	}

	private fun getCurrentTimeRange(): Time? = schema.times
		.firstOrNull {
			currentLocalTime in it.range
		}

}
