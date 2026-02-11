package me.maxos.votive.dangerMine.event.listener.player

import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import me.maxos.votive.dangerMine.utils.bukkit.Scheduler.runSyncTaskLater
import net.raidstone.wgevents.events.RegionEnteredEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.util.Vector

class EnterRegionListener(
	private val mineManager: MineManager
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
				player.sendMessage("${mine.schema.name} закрыта!")
				e.isCancelled = true
				runSyncTaskLater(1, true) {
					player.knockbackPlayer()
				}
			}

		} else sendDebug("Шахта с таким регионом не найдена")
	}

	private fun Player.knockbackPlayer(horizontal: Double = 1.0, vertical: Double = 0.5) {
		val direction = this.location.direction.normalize()
		val horizontalVelocity = direction.multiply(-horizontal)
		val verticalVelocity = Vector(0.0, vertical, 0.0)
		// откидываем плеера с инверсией
		this.velocity = horizontalVelocity.add(verticalVelocity)
	}

}