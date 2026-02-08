package me.maxos.votive.dangerMine.event

import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.utils.Debuger.sendDebug
import net.raidstone.wgevents.events.RegionEnteredEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.util.Vector

class PlayerEnterMineEvent(
	val player: Player,
	val mineName: String
) : Event() {

	companion object {
		private val handlers = HandlerList()

		@JvmStatic
		fun getHandlerList(): HandlerList {
			return handlers
		}
	}

	override fun getHandlers(): HandlerList {
		return getHandlerList()
	}
}

class PlayerEnterRegion(
	private val mineManager: MineManager
): Listener {

	@EventHandler
	fun onEnterRegion(e: RegionEnteredEvent) {
		val regionName = e.regionName
		val player = e.player ?: return
		if (mineManager.inRegion(regionName)) {

			val mine = mineManager.getMine(regionName) ?: return
			if (!mine.isOpen) {
				player.sendMessage("${mine.schema.name} закрыта!")
				e.isCancelled = true
				player.knockbackPlayer()
			} else {
				// если удачно зашли в шахту
				val event = PlayerEnterMineEvent(player, mine.schema.name)
				Bukkit.getPluginManager().callEvent(event)
			}
		}
	}

	private fun Player.knockbackPlayer(horizontal: Double = 1.0, vertical: Double = 0.5) {
		val direction = this.location.direction.normalize()
		val horizontalVelocity = direction.multiply(-horizontal)
		val verticalVelocity = Vector(0.0, vertical, 0.0)
		// откидываем плеера с инверсией
		this.velocity = horizontalVelocity.add(verticalVelocity)
	}

	@EventHandler
	fun success(e: PlayerEnterMineEvent) {
		sendDebug("Игрок ${e.player.name} успешно зашёл в шахту ${e.mineName}")
	}

}