package me.maxos.votive.dangerMine.models

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class Drop(
	val material: Material,
	val chance: Int,
	val dropAmountRange: IntRange
) {
	private val _itemStack = ItemStack(material)
	val itemStack: ItemStack get() = _itemStack.clone().apply {
		this.amount = dropAmountRange.random()
	}
}