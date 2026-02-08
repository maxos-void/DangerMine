package me.maxos.votive.dangerMine.file.config

import me.maxos.votive.dangerMine.file.FileManager
import me.maxos.votive.dangerMine.model.Drop
import me.maxos.votive.dangerMine.model.MineSchema
import me.maxos.votive.dangerMine.model.Time
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import me.maxos.votive.dangerMine.utils.TimeParser.toTimer
import me.maxos.votive.dangerMine.utils.logError
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

class ConfigManager(
	private val fileManager: FileManager,
) {
	companion object {
		const val MAIN_SECTION_NAME = "mines"
	}

	private lateinit var config: FileConfiguration

	init {
		initConfig()
	}

	fun reloadConfig() {
		fileManager.reloadConfig()
		initConfig()
	}

	private fun initConfig() {
		config = fileManager.getConfig()
		sendDebug("КОНФИГ NULL?: ${config == null}")
		sendDebug("$config")
	}

	fun createMines(): HashMap<String, MineSchema> {
		val minesMap = hashMapOf<String, MineSchema>()
		val mainSection = (getRequiredSection(MAIN_SECTION_NAME) ?: return minesMap)
		mainSection.getKeys(false).forEach { mineId ->

			val mineSection = getRequiredSection(
				mainSection, mineId
			) ?: return@forEach

			val breakageMaterialsSection = getRequiredSection(
				mineSection, "breakage-materials"
			) ?: return@forEach

			val blockMaterials = breakageMaterialsSection.getKeys(false)
			val blocksSections = blockMaterials.mapNotNull {
				getRequiredSection(breakageMaterialsSection,it) ?: run {
					sendDebug("blocksSections null")
					return@mapNotNull null
				}
			}

			val drop = hashMapOf<Material, HashSet<Drop>>().apply {
				blocksSections.forEach { block ->
					val material = getMaterial(block.name) ?: return@forEach
					val drop = getBlockDrops(block)
					put(material, drop)
				}
			}

			val isAnyDate = mineSection.getString("work-dates") == "any"
			val dates: HashSet<Int> = mineSection.getIntegerList("work-dates").toHashSet()
			val times = mineSection.getStringList("timers").mapNotNull {
				val timePair = it.toTimer() ?: run {
					logError("Ошибка парсинга времени: $it")
					return@mapNotNull null
				}
				Time(
					timePair.first,
					timePair.second
				)
			}.toHashSet()

			val regionName = mineSection.getString("region-name") ?: return@forEach

			minesMap[regionName] = MineSchema(
				mineId,
				mineSection.getString("name") ?: run {
					sendDebug("mineSection.getString(\"name\") == null!!!")
					return@forEach
				},
				regionName,
				mineSection.getInt("recovery"),
				mineSection.getInt("radius"),
				mineSection.getInt("min-players"),
				isAnyDate,
				if (isAnyDate) hashSetOf() else dates,
				blockMaterials.mapNotNull { Material.getMaterial(it) }.toHashSet(),
				drop,
				times,
			)
		}

		return minesMap
	}

	private fun getMaterial(name: String): Material? = Material.getMaterial(name) ?: run {
		logError("Не удалось получить материал $name")
		null
	}

	private fun getBlockDrops(blockSection: ConfigurationSection): HashSet<Drop> {
		getRequiredSection(blockSection, "drop-materials")
			?.also {
				return it.getKeys(false).mapNotNull { item ->
					val itemSection = getRequiredSection(it, item)
						?: run {
							sendDebug("drop-materials null (itemSection)")
							return@mapNotNull null
						}
					createDrop(itemSection)
						?: run {
							sendDebug("drop-materials null (createDrop)")
							return@mapNotNull null
						}
				}.toHashSet()
			}

		return hashSetOf()
	}

	private fun createDrop(itemSection: ConfigurationSection): Drop? {
		val chance = itemSection.getInt("chance")
		val amountSection = getRequiredSection(itemSection, "drop-amount") ?: run {
			sendDebug("drop-amounts null")
			return null
		}

		return Drop(
			Material.getMaterial(itemSection.name)
				?: run {
					logError("Не удалось получить материал ${itemSection.name}")
					return null
				},
			chance,
			amountSection.let {
				it.getInt("min")..it.getInt("max")
			}
		)
	}



	private fun getRequiredSection(parentSection: ConfigurationSection, name: String): ConfigurationSection? {
		val section = parentSection.getConfigurationSection(name)
		if (section == null) {
			logError("Секция конфига $name повреждена!")
			return null
		}
		return section
	}
	private fun getRequiredSection(name: String): ConfigurationSection? {
		val section = config.getConfigurationSection(name)
		if (section == null) {
			logError("Секция конфига $name повреждена!")
			return null
		}
		return section
	}
}