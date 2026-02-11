package me.maxos.votive.dangerMine.event.listener.mine

import me.maxos.votive.dangerMine.event.custom.MineCloseEvent
import me.maxos.votive.dangerMine.region.PlayerRegion.getMineRegions
import me.maxos.votive.dangerMine.region.PlayerRegion.inMineRegion
import net.raidstone.wgevents.WorldGuardEvents
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