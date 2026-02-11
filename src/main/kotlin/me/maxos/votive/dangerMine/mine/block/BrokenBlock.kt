package me.maxos.votive.dangerMine.mine.block

import me.maxos.votive.dangerMine.mine.Mine
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block

data class BrokenBlock(
	val block: Block,
	val oldType: Material,
	val mine: Mine
) {
	val loc = block.location.toBlockLocation().clone()
}