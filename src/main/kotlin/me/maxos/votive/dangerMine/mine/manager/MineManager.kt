package me.maxos.votive.dangerMine.mine.manager

import me.maxos.votive.dangerMine.file.config.ConfigManager
import me.maxos.votive.dangerMine.mine.Mine
import me.maxos.votive.dangerMine.mine.manager.MineManager.Region.regionNames
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import me.maxos.votive.dangerMine.utils.bukkit.Scheduler.runAsyncTaskTimer
import me.maxos.votive.dangerMine.utils.bukkit.Scheduler.stopTask

class MineManager(
	private val configManager: ConfigManager,
	private val timerManager: TimerManager
) {

	// получаем схемы шахт из конфигурации и создаём на их основе функциональные шахты
	private val minesByRegion: HashMap<String, Mine> = hashMapOf()
	private fun initMines() {
		minesByRegion.putAll(
			configManager.createMines()
				.mapValuesTo(HashMap()) {
						(_, schema) ->
					sendDebug("$schema ЗАРЕГИСТРИРОВАНО")
					Mine(schema, timerManager, configManager)
				}
		)
		regionNames = minesByRegion.keys.toHashSet()
		sendDebug("Регионы шахт: $regionNames")
	}

	// сет с названиями регионов всех шахт (необходимо для ивентов)
	companion object Region {
		var regionNames: HashSet<String> = hashSetOf()
			private set
	}

	init {
		initMines()
		pinger()
	}

	val mines: MutableCollection<Mine> = minesByRegion.values
	val minesNames = minesByRegion.values.map { it.schema.id }.toHashSet()

	fun getMine(regionName: String): Mine? = minesByRegion[regionName]
	fun getMineByName(mineName: String): Mine? = mines.firstOrNull { it.schema.id == mineName }

	fun inRegion(regionName: String): Boolean = regionNames.contains(regionName)

	private var taskId = -1
	private fun pinger() {
		taskId = runAsyncTaskTimer(30) {
			val relevanceDate = timerManager.relevanceDate()
			mines.forEach {	mine ->
				if (!relevanceDate) mine.updateDate()
				mine.ping()
			}
		}
	}

	fun reloadMines() {
		stopTask(taskId)
		update()
		pinger()
	}

	private fun update() {
		minesByRegion.clear()
		initMines()
		minesNames.apply {
			clear()
			addAll(minesByRegion.values.map { it.schema.id })
		}
	}

}