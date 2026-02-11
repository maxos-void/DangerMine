package me.maxos.votive.dangerMine.api.customevent

import me.maxos.votive.dangerMine.mine.Mine
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class MineEnterEvent(
	val player: Player,
	val mine: Mine
) : Event() {

	companion object {
		private val HANDLERS = HandlerList()

		@JvmStatic
		fun getHandlerList(): HandlerList = HANDLERS
	}

	override fun getHandlers(): HandlerList = HANDLERS
}