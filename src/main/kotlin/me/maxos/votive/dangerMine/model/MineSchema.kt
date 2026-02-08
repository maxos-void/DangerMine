package me.maxos.votive.dangerMine.model

import org.bukkit.Material

data class MineSchema(
	val id: String,
	val name: String,
	val regionName: String,
	val recovery: Int,
	val checkRadius: Int,
	val minPlayers: Int,
	val isAnyDate: Boolean = false,
	val workDate: HashSet<Int>,
	val breakageMaterials: HashSet<Material>,
	val drop: HashMap<Material, HashSet<Drop>>,
	val times: HashSet<Time>,
) {
	val ranges = times.map { it.range }.toHashSet()
}