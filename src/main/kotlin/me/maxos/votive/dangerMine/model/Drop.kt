package me.maxos.votive.dangerMine.model

import org.bukkit.Material

data class Drop(
	val material: Material,
	val chance: Int,
	val dropAmountRange: IntRange
)