package me.maxos.votive.dangerMine.listeners.mine

import me.maxos.votive.dangerMine.api.customevent.MineCloseEvent
import me.maxos.votive.dangerMine.extensions.PlayerExtension.inMineRegion
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MineCloseListener: Listener {

	@EventHandler
	fun onMineClose(e: MineCloseEvent) {
		val regionName = e.mine.schema.regionName
		val entranceLoc = e.mine.entranceLoc
		val world = Bukkit.getWorld(e.mine.schema.liteLocation.world)

		world?.players?.forEach { player ->
			if (player.inMineRegion(regionName)) {
				player.teleport(entranceLoc ?: player.bedLocation)
			}
		}
	}

}