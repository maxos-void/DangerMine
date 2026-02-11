package me.maxos.votive.dangerMine

import me.maxos.votive.dangerMine.command.MineCmdExecutor
import me.maxos.votive.dangerMine.command.MineTabCompleter
import me.maxos.votive.dangerMine.listeners.mine.MineCloseListener
import me.maxos.votive.dangerMine.listeners.player.BlockBreakListener
import me.maxos.votive.dangerMine.listeners.player.EnterRegionListener
import me.maxos.votive.dangerMine.listeners.player.JoinEventListener
import me.maxos.votive.dangerMine.file.FileManager
import me.maxos.votive.dangerMine.file.config.ConfigManager
import me.maxos.votive.dangerMine.mine.block.BrokenBlockScheduler
import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.mine.manager.TimerManager
import me.maxos.votive.dangerMine.placeholderapi.PlaceholderHook
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import me.maxos.votive.dangerMine.utils.Scheduler
import me.maxos.votive.dangerMine.utils.Scheduler.stopAllTask
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class DangerMine : JavaPlugin() {

	private lateinit var settings: FileManager
	private lateinit var configManager: ConfigManager

	private lateinit var timerManager: TimerManager
	private lateinit var mineManager: MineManager
	private lateinit var brokenBlockScheduler: BrokenBlockScheduler

	private lateinit var enterRegionListener: EnterRegionListener
	private lateinit var blockBreakListener: BlockBreakListener
	private lateinit var joinEventListener: JoinEventListener
	private lateinit var mineCloseListener: MineCloseListener

	private lateinit var executor: MineCmdExecutor
	private lateinit var tabCompleter: MineTabCompleter

	private lateinit var placeholderHook: PlaceholderHook

	override fun onEnable() {

		Scheduler.initialization(this)
		settings = FileManager(this, "settings.yml") // создание файла

		configManager = ConfigManager(settings)

		timerManager = TimerManager()
		mineManager = MineManager(configManager, timerManager)
		brokenBlockScheduler = BrokenBlockScheduler()

		enterRegionListener = EnterRegionListener(mineManager).apply {
			register()
		}
		blockBreakListener = BlockBreakListener(mineManager, brokenBlockScheduler).apply {
			register()
		}
		joinEventListener = JoinEventListener(mineManager).apply {
			register()
		}
		mineCloseListener = MineCloseListener().apply {
			register()
		}

		executor = MineCmdExecutor(this, mineManager, brokenBlockScheduler)
		tabCompleter = MineTabCompleter().apply { initNames() }
		val cmd = Bukkit.getPluginCommand("dangermine")
		cmd?.setExecutor(executor)
		cmd?.tabCompleter = tabCompleter

		placeholderHook = PlaceholderHook(mineManager)

	}

	override fun onDisable() {
		brokenBlockScheduler.forceResetGlobal(null)
	}

	fun onReload() {
		stopAllTask()
		brokenBlockScheduler.forceResetGlobal(null)
		configManager.reloadConfig()
		mineManager.reloadMines()
		tabCompleter.initNames()
	}

	private fun Listener.register() {
		val manager = Bukkit.getPluginManager()
		manager.registerEvents(this, this@DangerMine)
	}

}
