package me.maxos.votive.dangerMine.placeholderapi

import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.placeholderapi.ExpansionInfo.AUTHOR
import me.maxos.votive.dangerMine.placeholderapi.ExpansionInfo.IDENTIFIER
import me.maxos.votive.dangerMine.placeholderapi.ExpansionInfo.VERSION
import org.bukkit.OfflinePlayer
import kotlin.text.startsWith

class MineExpansion(
	private val mineData: MineDataProvider
): PlaceholderExpansion() {

	override fun getIdentifier(): String = IDENTIFIER
	override fun getAuthor(): String = AUTHOR
	override fun getVersion(): String = VERSION
	override fun persist(): Boolean = true

	override fun onRequest(player: OfflinePlayer?, params: String): String? {
		return when(params.lowercase()) {
			"count" -> mineData.getCount()
			"count_open" -> mineData.getCountOpen()
			else -> {
				when {
					params.startsWith("time_open_") -> mineData.beforeOpening(
						params.removePrefix("time_open_")
					)
					params.startsWith("status_") -> mineData.status(
						params.removePrefix("status_")
					)
					params.startsWith("time_close_") -> mineData.beforeClosing(
						params.removePrefix("time_close_")
					)
					else -> null
				}
			}
		}
	}

}