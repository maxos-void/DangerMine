package me.maxos.votive.dangerMine.extensions

import me.maxos.votive.dangerMine.mine.manager.MineManager.Region
import net.raidstone.wgevents.WorldGuardEvents
import org.bukkit.entity.Player

object PlayerExtension {

	fun Player.getMineRegions(): Set<String> {
		val regions = WorldGuardEvents.getRegionsNames(this.uniqueId)
		return regions intersect Region.regionNames
	}

	fun Player.inMineRegion(regionName: String): Boolean {
		return WorldGuardEvents.isPlayerInAnyRegion(
			this.uniqueId, regionName
		)
	}

	fun Player.nearbyPlayers(radius: Int): Int {
		return this.location.getNearbyPlayers(radius.toDouble()).size
	}

}