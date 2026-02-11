package me.maxos.votive.dangerMine.api.customevent

import me.maxos.votive.dangerMine.mine.Mine
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

class MineBlockBreakEvent(
	val player: Player,
	val mine: Mine,
	val block: Block,
	val dropItem: ItemStack
) : Event() {

	companion object {
		private val HANDLERS = HandlerList()

		@JvmStatic
		fun getHandlerList(): HandlerList = HANDLERS
	}

	override fun getHandlers(): HandlerList = HANDLERS
}