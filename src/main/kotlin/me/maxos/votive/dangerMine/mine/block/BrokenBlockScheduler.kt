package me.maxos.votive.dangerMine.mine.block

import me.maxos.votive.dangerMine.mine.Mine
import me.maxos.votive.dangerMine.utils.bukkit.Scheduler.runSyncTaskLater
import me.maxos.votive.dangerMine.utils.bukkit.Scheduler.stopTask
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BrokenBlockScheduler {

	private val blocksToMine = hashMapOf<Mine, HashSet<BrokenBlock>>()
	private val tasks = hashMapOf<BrokenBlock, Int>()

	fun addBlock(mine: Mine, block: Block, oldMaterial: Material, recovery: Int = mine.schema.recovery) {
		setMaterial(block)
		BrokenBlock(block, oldMaterial, mine).let {
			blocksToMine.getOrPut(mine) { HashSet() }.add(it)
			schedule(mine, it, recovery)
		}
	}

	fun forceResetGlobal(sender: CommandSender?) {
		val blocks = blocksToMine.values.flatten()
		val total = blocks.size
		blocks.forEach {
			reset(it)
		}
		forceRemoveGlobal()
		sender?.sendMessage("Блоков во всех шахтах восстановлено: $total")
	}

	fun forceResetMine(sender: CommandSender, mine: Mine) {
		val total = blocksToMine[mine]?.size ?: 0
		blocksToMine[mine]?.forEach {
			reset(it)
		}
		forceRemoveMine(mine)
		sender.sendMessage("В шахте \"${mine.schema.name}\" восстановлено блоков: $total")
	}

	fun forceResetRadius(mine: Mine, player: Player, radius: Int = 30) {
		if (radius > 50) {
			player.sendMessage("Радиус должен быть не больше 50 блоков!")
			return
		}

		blocksToMine[mine]?.filter { player.location.clone().distance(it.loc) <= radius }.let {
			it?.forEach { block ->
				reset(block)
				removeBlock(mine, block)
			}
			player.sendMessage("Блоков восстановлено: ${it?.size ?: 0}")
		}
	}

	private fun setMaterial(block: Block, material: Material = Material.STONE) {
		block.type = material
	}

	private fun schedule(mine: Mine, brokenBlock: BrokenBlock, recovery: Int) {
		val task = runSyncTaskLater(recovery) {
			reset(brokenBlock)
			removeBlock(mine, brokenBlock)
		}
		tasks[brokenBlock] = task
	}

	private fun reset(brokenBlock: BrokenBlock) {
		stopTask(tasks[brokenBlock])
		tasks.remove(brokenBlock)
		brokenBlock.block.type = brokenBlock.oldType
	}

	private fun removeBlock(mine: Mine, block: BrokenBlock) {
		blocksToMine[mine]?.remove(block)
	}

	private fun forceRemoveMine(mine: Mine) {
		blocksToMine[mine]?.clear()
	}

	private fun forceRemoveGlobal() {
		blocksToMine.clear()
	}

}