package me.maxos.votive.dangerMine.event.custom

import me.maxos.votive.dangerMine.mine.Mine
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

class MineOpenEvent(
	val mine: Mine
) : Event() {

	companion object {
		private val HANDLERS = HandlerList()

		@JvmStatic
		fun getHandlerList(): HandlerList = HANDLERS
	}

	override fun getHandlers(): HandlerList = HANDLERS
}