package me.maxos.votive.dangerMine

import me.maxos.votive.dangerMine.command.MineCmdExecutor
import me.maxos.votive.dangerMine.command.MineTabCompleter
import me.maxos.votive.dangerMine.event.PlayerEnterRegion
import me.maxos.votive.dangerMine.file.FileManager
import me.maxos.votive.dangerMine.file.config.ConfigManager
import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.mine.manager.TimerManager
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import me.maxos.votive.dangerMine.utils.bukkit.Scheduler
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class DangerMine : JavaPlugin() {

	private lateinit var settings: FileManager
	private lateinit var configManager: ConfigManager

	private lateinit var timerManager: TimerManager
	private lateinit var mineManager: MineManager

	private lateinit var playerEnterRegion: PlayerEnterRegion

	private lateinit var executor: MineCmdExecutor
	private lateinit var tabCompleter: MineTabCompleter

	override fun onEnable() {
		sendDebug("НАЧИНАЕМ ЗАПУСК ПУШКИ-ПЕТАРДЫ!!!")
		Scheduler.initialization(this)
		settings = FileManager(this, "settings.yml") // создание файла
		sendDebug("НАЧИНАЕМ ЗАПУСК ПУШКИ-ПЕТАРДЫ!!!")
		configManager = ConfigManager(settings)

		timerManager = TimerManager()
		mineManager = MineManager(configManager, timerManager)

		playerEnterRegion = PlayerEnterRegion(mineManager).apply { register() }

		executor = MineCmdExecutor(this)
		tabCompleter = MineTabCompleter()
		val cmd = Bukkit.getPluginCommand("dangermine")
		cmd?.setExecutor(executor)
		cmd?.tabCompleter = tabCompleter

	}

	override fun onDisable() {
		TODO()
	}

	fun onReload() {
		configManager.reloadConfig()
		mineManager.reloadMines()
	}

	private fun Listener.register() {
		val manager = Bukkit.getPluginManager()
		manager.registerEvents(this, this@DangerMine)
	}

}
