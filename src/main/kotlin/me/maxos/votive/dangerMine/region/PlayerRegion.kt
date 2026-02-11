package me.maxos.votive.dangerMine.region

import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.mine.manager.MineManager.Region
import net.raidstone.wgevents.WorldGuardEvents
import org.bukkit.entity.Player

object PlayerRegion {

	fun Player.getMineRegions(): Set<String> {
		val regions = WorldGuardEvents.getRegionsNames(this.uniqueId)
		return regions intersect Region.regionNames
	}

	fun Player.inMineRegion(regionName: String): Boolean {
		return WorldGuardEvents.isPlayerInAnyRegion(
			this.uniqueId, regionName
		)
	}

}