package me.maxos.votive.dangerMine.placeholderapi

import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.utils.logInfo
import org.bukkit.Bukkit

class PlaceholderHook(
	private val mineManager: MineManager
) {

	companion object {
		const val PLUGIN_NAME = "PlaceholderAPI"
	}

	fun initExpansion() {
		if (Bukkit.getPluginManager().isPluginEnabled(PLUGIN_NAME)) {
			MineExpansion(MineDataProvider(mineManager)).register()
			logInfo("PlaceholderAPI успешно интегрирован!")
		} else logInfo("PlaceholderAPI не был обнаружен!")
	}

}