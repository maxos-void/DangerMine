package me.maxos.votive.dangerMine.event.listener.player

import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.region.PlayerRegion.getMineRegions
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEventListener(
	private val mineManager: MineManager,
): Listener {

	@EventHandler
	fun onPlayerJoin(e: PlayerJoinEvent) {
		val player = e.player
		val regions = player.getMineRegions()

		if (regions.isNotEmpty()) {
			regions.forEach { region ->
				mineManager.getMine(region)?.let { mine ->

					if (!mine.isOpen) {
						player.sendMessage("Шахта закрыта!")
						player.teleport(mine.entranceLoc ?: player.bedLocation)
					}

				}
			}
		}
	}

}