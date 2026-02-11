package me.maxos.votive.dangerMine.file

import me.maxos.votive.dangerMine.utils.logError
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

abstract class Config(
	protected val fileManager: FileManager
) {

	protected lateinit var config: FileConfiguration

	init {
		initConfig()
	}

	fun reloadConfig() {
		fileManager.reloadConfig()
		initConfig()
	}

	protected fun initConfig() {
		config = fileManager.getConfig()
	}

	protected fun getRequiredSection(parentSection: ConfigurationSection, name: String): ConfigurationSection? {
		val section = parentSection.getConfigurationSection(name)
		if (section == null) {
			logError("Секция конфига $name повреждена!")
			return null
		}
		return section
	}
	protected fun getRequiredSection(name: String): ConfigurationSection? {
		val section = config.getConfigurationSection(name)
		if (section == null) {
			logError("Секция конфига $name повреждена!")
			return null
		}
		return section
	}

}