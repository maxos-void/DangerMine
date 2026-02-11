package me.maxos.votive.dangerMine.command

import me.maxos.votive.dangerMine.DangerMine
import me.maxos.votive.dangerMine.mine.block.BrokenBlockScheduler
import me.maxos.votive.dangerMine.mine.manager.MineManager
import me.maxos.votive.dangerMine.region.PlayerRegion.getMineRegions
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MineCmdExecutor(
	private val plugin: DangerMine,
	private val mineManager: MineManager,
	private val brokenBlockScheduler: BrokenBlockScheduler
): CommandExecutor {
	override fun onCommand(
		sender: CommandSender,
		command: Command,
		label: String,
		args: Array<out String>?
	): Boolean {

		when(args?.get(0) ?: return false) {

			"reload" -> {
				plugin.onReload()
				return true
			}

			"reset-radius" -> {
				val player = sender.toPlayer() ?: return true
				val radius = args.getOrNull(1)?.toIntOrNull() ?: return false

				player.getMineRegions().forEach { mineRegion ->
					mineManager.getMine(mineRegion)?.let { mine ->
						brokenBlockScheduler.forceResetRadius(
							mine, player, radius
						)
					} ?: run {
						notMineToRegion(player)
						return true
					}
				}
				return true
			}

			"reset-mine" -> {
				val mineName = args.getOrNull(1) ?: return false
				val mine = mineManager.getMineByName(mineName) ?: run {
					notMineToName(sender)
					return true
				}
				brokenBlockScheduler.forceResetMine(sender, mine)
				return true
			}

			"reset-global" -> {
				brokenBlockScheduler.forceResetGlobal(sender)
				return true
			}

			"set-entrance" -> {
				val player = sender.toPlayer() ?: return true
				val mineName = args.getOrNull(1) ?: return false
				val mine = mineManager.getMineByName(mineName) ?: run {
					notMineToName(sender)
					return true
				}

				val loc = player.location.clone()
				mine.setLocation(loc)
				player.sendMessage("Вы успешно изменили точку входа шахты!")
				return true
			}

			else -> return false
		}
	}

	private fun CommandSender.toPlayer(): Player? {
		return run {
			this.sendMessage("Команда доступна только игрокам!")
			this as? Player
		}
	}

	private fun notMineToRegion(sender: CommandSender) {
		sender.sendMessage("Вы не находитесь в регионе существующей шахты!")
	}
	private fun notMineToName(sender: CommandSender) {
		sender.sendMessage("Шахта с таким названием не обнаружена!")
	}
}