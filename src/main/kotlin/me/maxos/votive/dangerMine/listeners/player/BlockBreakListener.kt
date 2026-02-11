package me.maxos.votive.dangerMine.listeners.player

import me.maxos.votive.dangerMine.api.EventApiManager.callEvent
import me.maxos.votive.dangerMine.api.customevent.MineBlockBreakEvent
import me.maxos.votive.dangerMine.mine.Mine
import me.maxos.votive.dangerMine.mine.block.BrokenBlockScheduler
import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.models.Drop
import me.maxos.votive.dangerMine.extensions.PlayerExtension.getMineRegions
import me.maxos.votive.dangerMine.utils.Scheduler.runSyncTaskLater
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class BlockBreakListener(
	private val mineManager: MineManager,
	private val brokenBlockScheduler: BrokenBlockScheduler
): Listener {

	private val breakingGmPlayer = hashSetOf<Player>()

	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
	fun onBlockBreak(e: BlockBreakEvent) {

		val player = e.player
		val block = e.block

		val regions = player.getMineRegions()
		if (regions.isNotEmpty()) {
			regions.forEach { region ->
				mineManager.getMine(region)?.let { mine ->

					if (checkGm(player)) return
					e.isCancelled = true

					if (!mine.isOpen) {
						player.sendMessage("Шахта закрыта!")
						return
					}

					val blockType = block.type
					val drop = checkMaterial(mine, blockType) ?: return
					blockIsBroken(mine, block, blockType, drop, player)

				}
			}
		}
	}

	private fun checkMaterial(mine: Mine, blockType: Material): Drop? {
		return mine.schema.drop[blockType]?.weightedRandom()
	}

	private fun HashSet<Drop>.weightedRandom(): Drop? {
		val total = sumOf { it.chance }
		var randomPoint = Random.nextInt(total)

		return firstOrNull { drop ->
			randomPoint -= drop.chance
			randomPoint < 0
		}
	}

	private fun blockIsBroken(
		mine: Mine, block: Block,
		oldMaterial: Material, drop: Drop,
		player: Player
	) {
		brokenBlockScheduler.addBlock(mine, block, oldMaterial)
		val dropItem = spawnDrop(block.location.clone(), drop)

		callEvent(MineBlockBreakEvent(player, mine, block, dropItem))
	}

	private fun spawnDrop(loc: Location, drop: Drop): ItemStack {
		val item = drop.itemStack
		loc.world.dropItemNaturally(loc, item)
		return item
	}

	private fun checkGm(player: Player): Boolean {
		if (player.gameMode == GameMode.CREATIVE) {
			if (!breakingGmPlayer.contains(player)) {
				player.sendMessage("Вы ломаете блоки в шахте, находясь в креативе!")
				player.sendMessage("Они не будут восстановлены!")
				breakingGmPlayer.add(player)
				runSyncTaskLater(60) {
					breakingGmPlayer.remove(player)
				}
			}

			return true

		} else return false
	}

}