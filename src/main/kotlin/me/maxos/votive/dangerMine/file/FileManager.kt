package me.maxos.votive.dangerMine.file

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class FileManager(
	private val plugin: JavaPlugin,
	private val fileName: String
) {
	private val dataFolder = plugin.dataFolder
	private val logger = plugin.logger

	private var configFile: File = File(dataFolder, fileName)
	private lateinit var config: FileConfiguration

	init {
		saveDefaultConfig()
		loadConfig()
	}

	// Сохраняем дефолтный конфиг
	private fun saveDefaultConfig() {
		if (!configFile.exists()) {
			plugin.saveResource(fileName, false)
			logger.info("Создан файл $fileName")
		}
	}

	private fun loadConfig(): Boolean {
		config = YamlConfiguration.loadConfiguration(configFile)
		setDefaultConfig()
		return isNotEmptyConfig()
	}

	private fun setDefaultConfig() {
		plugin.getResource(fileName)?.let { defaultStream ->
			val defaultConfig = YamlConfiguration.loadConfiguration(
				InputStreamReader(defaultStream, StandardCharsets.UTF_8)
			)
			config.setDefaults(defaultConfig)
		}
	}

	private fun isNotEmptyConfig(): Boolean {
		config.getKeys(false).isNotEmpty().let {
			if (!it) logger.warning("Конфигурационный файл $fileName пуст!")
			return it
		}
	}

	// Перезагрузка конфига
	fun reloadConfig() {
		if (loadConfig())
			logger.info("Конфигурационный файл $fileName успешно перезагружен!")
		else
			logger.warning("Конфигурационный файл $fileName при перезагрузке оказался пуст!")
	}

	// Получение конфига
	fun getConfig(): FileConfiguration = config

	fun saveConfig() {
		try {
			config.save(configFile)
			logger.info("Конфигурационный файл $fileName сохранен!")
		} catch (e: IOException) {
			logger.severe("Не удалось сохранить конфигурационный файл $fileName: ${e.message}")
		}
	}

	// Установить значение и сохранить
	fun setValue(path: String, value: Any?) {
		config.set(path, value)
		saveConfig()
	}
	// установить целую секцию
	fun setSection(path: String, section: Map<String, Any>) {
		config.set(path, section)
		saveConfig()
	}

}