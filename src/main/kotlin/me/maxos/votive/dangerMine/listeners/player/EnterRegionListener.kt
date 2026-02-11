package me.maxos.votive.dangerMine.listeners.player

import me.maxos.votive.dangerMine.api.EventApiManager.callEvent
import me.maxos.votive.dangerMine.api.customevent.MineEnterEvent
import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.extensions.PlayerExtension.nearbyPlayers
import me.maxos.votive.dangerMine.file.config.msg.Messages
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import me.maxos.votive.dangerMine.utils.Scheduler.runSyncTaskLater
import net.raidstone.wgevents.events.RegionEnteredEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.util.Vector

class EnterRegionListener(
	private val mineManager: MineManager,
	private val messages: Messages
): Listener {

	@EventHandler
	fun onEnterRegion(e: RegionEnteredEvent) {
		val regionName = e.regionName
		val player = e.player ?: return

		sendDebug("Игрок ${player.name} зашёл в регион $regionName")

		if (mineManager.inRegion(regionName)) {

			sendDebug("Шахта с таким регионом найдена")

			val mine = mineManager.getMine(regionName) ?: return
			if (!mine.isOpen) {
				player.sendMessage(messages.msg("closed"))
				e.isCancelled = true
				runSyncTaskLater(1, true) {
					player.knockbackPlayer()
				}
				return

			} else {
				val minPlayers = mine.schema.minPlayers
				if (minPlayers > 0) {
					val radius = mine.schema.checkRadius
					if (player.nearbyPlayers(radius) < minPlayers) {
						e.isCancelled = true
						player.sendMessage(
							messages.msg("no-players")
								.replace(
									"{min-players}",
									minPlayers.toString()
								)
						)
						return
					}
				}

				callEvent(MineEnterEvent(player, mine))
			}

		} else sendDebug("Шахта с таким регионом не найдена")
	}

	private fun Player.knockbackPlayer(horizontal: Double = 0.3, vertical: Double = 0.2) {
		val direction = this.location.direction.normalize()
		val horizontalVelocity = direction.multiply(-horizontal)
		val verticalVelocity = Vector(0.0, vertical, 0.0)
		// откидываем плеера с инверсией
		this.velocity = horizontalVelocity.add(verticalVelocity)
	}

}