package me.maxos.votive.dangerMine.file.config.mine

import me.maxos.votive.dangerMine.file.Config
import me.maxos.votive.dangerMine.file.FileManager
import me.maxos.votive.dangerMine.models.Drop
import me.maxos.votive.dangerMine.models.LiteLocation
import me.maxos.votive.dangerMine.models.MineSchema
import me.maxos.votive.dangerMine.models.Time
import me.maxos.votive.dangerMine.utils.ColorUtils
import me.maxos.votive.dangerMine.utils.Debuger
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import me.maxos.votive.dangerMine.utils.TimeParser.toTimer
import me.maxos.votive.dangerMine.utils.logError
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection

class MineConfigFactory(
	fileManager: FileManager,
): Config(fileManager) {
	companion object {
		const val MAIN_SECTION_NAME = "mines"
	}

	init {
		initConfig()
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
					Debuger.sendDebug("blocksSections null")
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
				ColorUtils.colorize(mineSection.getString("name") ?: run {
					sendDebug("mineSection.getString(\"name\") == null!!!")
					return@forEach
				}),
				regionName,
				mineSection.getInt("recovery"),
				mineSection.getInt("radius"),
				mineSection.getInt("min-players"),
				isAnyDate,
				if (isAnyDate) hashSetOf() else dates,
				blockMaterials.mapNotNull { Material.getMaterial(it) }.toHashSet(),
				drop,
				times,
				getLiteLocation(
					getRequiredSection(
						mineSection, "entrance"
					) ?: return@forEach
				) ?: return@forEach
			)
		}

		return minesMap
	}

	fun changeLoc(mineSchema: MineSchema, liteLocation: LiteLocation) {
		val mainSection = getRequiredSection(MAIN_SECTION_NAME) ?: return
		val mineSection = getRequiredSection(mainSection, mineSchema.id)
			?: return
		val locSection = getRequiredSection(mineSection, "entrance")
			?: return
		val patch = locSection.currentPath ?: return
		liteLocation.let { loc ->
			fileManager.setSection(
				patch,
				mapOf(
					"world" to loc.world,
					"x" to loc.x,
					"y" to loc.y,
					"z" to loc.z,
					"yaw" to loc.yaw.toDouble(),
					"pitch" to loc.pitch.toDouble(),
				)
			)
		}
	}

	private fun getLiteLocation(locSelection: ConfigurationSection): LiteLocation? {
		return try {
			LiteLocation(
				locSelection.getString("world")!!,
				locSelection.getDouble("x"),
				locSelection.getDouble("y"),
				locSelection.getDouble("z"),
				locSelection.getDouble("yaw").toFloat(),
				locSelection.getDouble("pitch").toFloat(),
			)
		} catch (e: Exception) {
			Debuger.sendDebug("Не получилось зарегистрировать локацию")
			null
		}
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
							Debuger.sendDebug("drop-materials null (itemSection)")
							return@mapNotNull null
						}
					createDrop(itemSection)
						?: run {
							Debuger.sendDebug("drop-materials null (createDrop)")
							return@mapNotNull null
						}
				}.toHashSet()
			}

		return hashSetOf()
	}

	private fun createDrop(itemSection: ConfigurationSection): Drop? {
		val chance = itemSection.getInt("chance")
		val amountSection = getRequiredSection(itemSection, "drop-amount") ?: run {
			Debuger.sendDebug("drop-amounts null")
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
}