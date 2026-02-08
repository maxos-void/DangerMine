package me.maxos.votive.dangerMine.mine.manager

import me.maxos.votive.dangerMine.file.config.ConfigManager
import me.maxos.votive.dangerMine.mine.Mine
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
					Mine(schema, timerManager)
				}
		)
	}

	init {
		initMines()
		pinger()
	}

	private val mines: MutableCollection<Mine> = minesByRegion.values

	fun getMine(regionName: String): Mine? = minesByRegion[regionName]

	// сет с названиями регионов всех шахт (необходимо для ивентов)
	private val regionNames: HashSet<String> by lazy {
		minesByRegion.keys.toHashSet()
	}
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
	}

}