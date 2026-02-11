package me.maxos.votive.dangerMine.listeners.player

import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.extensions.PlayerExtension.getMineRegions
import me.maxos.votive.dangerMine.file.config.msg.Messages
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEventListener(
	private val mineManager: MineManager,
	private val messages: Messages
): Listener {

	@EventHandler
	fun onPlayerJoin(e: PlayerJoinEvent) {
		val player = e.player
		val regions = player.getMineRegions()

		if (regions.isNotEmpty()) {
			regions.forEach { region ->
				mineManager.getMine(region)?.let { mine ->

					if (!mine.isOpen) {
						player.sendMessage(
							messages.msg("closed-teleport")
						)
						player.teleport(mine.entranceLoc ?: player.bedLocation)
					}

				}
			}
		}
	}

}